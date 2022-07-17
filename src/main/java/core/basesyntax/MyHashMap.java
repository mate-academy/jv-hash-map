package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int treshhold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private Node<K, V>[] table;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int size;

    public MyHashMap() {
        table = new Node[capacity];
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V item;
        private Node<K, V> next;

        public Node(K key, V item, Node<K, V> next) {
            this.key = key;
            this.item = item;
            this.next = next;
            hash = hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key);
        }

        @Override
        public int hashCode() {
            int h;
            return (key == null) ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> 16));
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == treshhold) {
            transfer();
        }
        Node<K, V> node = new Node<>(key, value, null);
        int index = getIndex(node.hash);
        if (table[index] == null) {
            table[index] = node;
            size++;
            return;
        }
        Node<K, V> temp = table[index];
        Node<K, V> prev = temp;
        while (temp != null) {
            if (node.hash == temp.hash && node.equals(temp)) {
                temp.item = value;
                return;
            }
            if (temp.next == null) {
                prev = temp;
            }
            temp = temp.next;
        }
        prev.next = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = new Node<>(key, null, null);
        int index = getIndex(node.hash);
        node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.item;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void transfer() {
        int oldCapacity = capacity;
        capacity *= 2;
        treshhold *= 2;
        Node<K, V>[] newTable = new Node[capacity];
        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                int index = getIndex(node.hash);
                Node<K, V> temp = newTable[index];
                if (temp == null) {
                    newTable[index] = new Node<>(node.key, node.item, null);
                } else {
                    while (temp.next != null) {
                        temp = temp.next;
                    }
                    temp.next = new Node<>(node.key, node.item, null);
                }
                node = node.next;
            }
        }
        table = newTable;
    }

    private int getIndex(int hash) {
        return hash % capacity;
    }
}
