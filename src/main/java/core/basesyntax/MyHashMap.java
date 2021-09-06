package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V> node;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int hash = Math.abs(hashCode(key)) % table.length;
        ensureCapacity();
        Node<K, V> node = table[hash];
        Node<K, V> putNode = new Node<>(key, value, null);
        int index = hash;
        if (table[index] == null) {
            table[index] = putNode;
            size++;
            return;
        }
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = putNode;
                size++;
                return;
            }
            node = node.next;
        }
    }

    public V getValue(K key) {
        int hashcode = hashCode(key);
        int index = Math.abs(hashcode) % table.length;
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    private void ensureCapacity() {
        int threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        if (size != threshold) {
            return;
        }
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[table.length * 2];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyHashMap<?, ?> myHashMap = (MyHashMap<?, ?>) o;
        return size == myHashMap.size && Objects.equals(node, myHashMap.node);
    }

    public int hashCode(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.value = value;
            this.key = key;
            this.next = next;
        }
    }
}
