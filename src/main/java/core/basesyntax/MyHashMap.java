package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int size;
    private Node<K, V>[] table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> node = table[hash(key)];

        if (node == null) {
            table[hash(key)] = new Node<>(hash(key), key, value, null);
            size++;
            return;
        }

        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.setValue(value);
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(hash(key), key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];

        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    private void resize() {

        if (size > (int)(capacity * DEFAULT_LOAD_FACTOR)) {
            capacity = capacity * 2;
            transfer();
        }
    }

    private void transfer() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];

        for (Node<K, V> oldNode : oldTable) {
            Node<K, V> node = oldNode;
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()) % capacity);
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            return this.value = value;
        }
    }
}
