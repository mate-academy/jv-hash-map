package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private int capacity;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = getHash(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> previousNode = table[hash];
        if (previousNode == null) {
            table[hash] = newNode;
            size++;
            return;
        }
        while (previousNode != null) {
            if (Objects.equals(key, previousNode.key)) {
                previousNode.value = value;
                return;
            }
            if (previousNode.next == null) {
                previousNode.next = newNode;
                size++;
                return;
            }
            previousNode = previousNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> getNode = table[getHash(key) % capacity];
        while (getNode != null) {
            if (Objects.equals(key, getNode.key)) {
                return getNode.value;
            }
            getNode = getNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private void resize() {
        if (size == threshold) {
            capacity = DEFAULT_CAPACITY * 2;
            threshold = (int) (capacity * LOAD_FACTOR);
            Node<K, V>[] nodes = table;
            table = (Node<K, V>[]) new Node[capacity];
            size = 0;
            for (Node<K, V> node : nodes) {
                if (node != null) {
                    put(node.key, node.value);
                    while (node.next != null) {
                        node = node.next;
                        put(node.key, node.value);
                    }
                }
            }
        }
    }
}
