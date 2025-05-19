package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final int MAXIMUM_CAPACITY = 1 << 30;
    private int treeifyThreshold = 12;
    private Node<K, V>[] table;
    private int capacity;
    private int size;

    @SuppressWarnings({"unchecked"})
    private void resize() {
        if (capacity >= MAXIMUM_CAPACITY) {
            return;
        }
        treeifyThreshold *= 2;
        capacity *= 2;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];

        for (Node<K, V> kvNode : oldTable) {
            if (kvNode != null) {
                Node<K, V> currentNode = kvNode;
                while (currentNode != null) {
                    Node<K, V> nextNode = currentNode.next;
                    currentNode.next = null; // Удаляем старую цепочку
                    putNode(currentNode);
                    currentNode = nextNode;
                }
            }
        }
    }

    private void putNode(Node<K, V> node) {
        int bucket = node.hash % capacity;
        if (table[bucket] == null) {
            table[bucket] = node;
        } else {
            Node<K, V> currentNode = table[bucket];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = node;
        }
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0) {
            table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
            capacity = DEFAULT_INITIAL_CAPACITY;
        }
        int hash = key == null ? 0 : Math.abs(key.hashCode());
        int bucket = hash % capacity;

        Node<K, V> currentNode = table[bucket];
        while (currentNode != null) {
            if (Objects.equals(currentNode.getKey(), key)) {
                currentNode.setValue(value);
                return;
            }
            currentNode = currentNode.next;
        }

        Node<K, V> newNode = new Node<>(hash, key, value, table[bucket]);
        table[bucket] = newNode;
        size++;

        if (size >= treeifyThreshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (capacity == 0) {
            return null;
        }
        int hash = key == null ? 0 : Math.abs(key.hashCode());
        int bucket = hash % capacity;
        Node<K, V> currentNode = table[bucket];
        while (currentNode != null) {
            if (Objects.equals(currentNode.getKey(), key)) {
                return currentNode.getValue();
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Map.Entry<?, ?> entry)) {
                return false;
            }
            return Objects.equals(key, entry.getKey())
                    && Objects.equals(value, entry.getValue());
        }

        public String toString() {
            return key + "=" + value;
        }
    }
}
