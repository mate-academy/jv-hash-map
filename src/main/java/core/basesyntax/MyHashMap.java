package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_GROW = 2;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int capacity = DEFAULT_CAPACITY;
    private double threshold = 12;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int hash = Math.abs(key == null ? 0 : (key.hashCode() % capacity));
        Node<K,V> node = table[hash];
        if (node == null) {
            table[hash] = new Node<>(key, value);
            size++;
            return;
        } else {
            while (node.next != null) {
                if (Objects.equals(node.getKey(),key)) {
                    node.setValue(value);
                    return;
                }
                node = node.next;
            }
            if (Objects.equals(node.getKey(),key)) {
                node.setValue(value);
                return;
            }
        }
        node.next = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = Math.abs(key == null ? 0 : key.hashCode() % capacity);
        Node<K,V> node = table[hash];

        if (node == null) {
            return null;
        }

        while (node != null) {
            if (Objects.equals(node.getKey(),key)) {
                return node.getValue();
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resizeIfNeeded() {
        if (size == threshold) {
            size = 0;
            Node<K,V>[] newArray = new Node[capacity * DEFAULT_GROW];
            Node<K,V>[] oldArray = table;
            table = newArray;
            capacity = capacity * DEFAULT_GROW;
            for (Node<K,V> node: oldArray) {
                if (node != null) {
                    put(node.getKey(), node.getValue());
                    while (node.next != null) {
                        node = node.next;
                        put(node.getKey(), node.getValue());
                    }
                }
            }
            threshold = capacity * DEFAULT_LOAD_FACTOR;
        }

    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            int hash = 17;
            hash = hash * 31 + (key == null ? 0 : key.hashCode());
            hash = hash * 31 + (value == null ? 0 : value.hashCode());
            hash = hash * 31 + (next == null ? 0 : next.hashCode());
            return Math.abs(hash);
        }
    }
}
