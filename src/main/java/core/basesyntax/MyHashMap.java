package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int capacity;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        capacity = DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size >= (table.length * DEFAULT_LOAD_FACTOR)) {
            resize();
        }
        int hash = hash(key);
        Node<K, V> nodeToPut = new Node<K, V>(hash, key, value, null);
        Node<K, V> node = table[hash];
        if (node == null) {
            table[hash] = nodeToPut;
            ++size;
            return;
        }
        while (node != null) {
            if (areEqualKeyAndHash(key, hash, node)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                break;
            }
            node = node.next;
        }
        node.next = nodeToPut;
        ++size;

    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node<K, V> node = table[hash];
        while (node != null) {
            if (areEqualKeyAndHash(key, hash, node)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] tempTable = new Node[table.length];
        System.arraycopy(table, 0, tempTable, 0, tempTable.length);
        capacity = capacity << 1;
        table = new Node[capacity];
        for (Node<K, V> element : tempTable) {
            while (element != null) {
                put(element.key, element.value);
                element = element.next;
            }
        }
    }

    private boolean areEqualKeyAndHash(K key, int hash, Node<K, V> node) {
        return (node.key == key)
                || node.hash == hash && (Objects.equals(key, node.key));
    }

    private int hash(K key) {
        int prevHash = (key == null) ? 0 : (key.hashCode() % capacity);
        return prevHash < 0 ? -prevHash : prevHash;
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public boolean equals(Node<K, V> o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Node<K, V> node = (Node<K, V>) o;

            return this.key == o.key
                    || this.key != null && this.key.equals(o.key)
                    && this.value == o.value
                    || this.value != null && this.value.equals(o.value);
        }
    }
}
