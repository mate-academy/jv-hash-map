package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final double RESIZE_FACTOR = 0.75;
    private Node<K, V>[] storage;
    private int size;

    public MyHashMap() {
        storage = (Node<K, V>[]) new Node[DEFAULT_SIZE];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> existingNode = getNodeByKey(key);
        if (existingNode != null) {
            existingNode.value = value;
            return;
        }
        ensureCapacity();
        insertNode(new Node<>(key, value));
        size++;
    }

    @Override
    public V getValue(K key) {
        final Node<K, V> existingNode = getNodeByKey(key);
        return existingNode == null ? null : existingNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNodeByKey(K key) {
        Node<K, V> current = storage[getBucketIndex(key)];
        do {
            if (current == null || Objects.equals(current.key, key)) {
                return current;
            }
            current = current.next;
        } while (true);
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % storage.length;
    }

    private void ensureCapacity() {
        if (size > RESIZE_FACTOR * storage.length) {
            Node<K, V>[] oldData = storage;
            storage = (Node<K, V>[]) new Node[storage.length << 1];
            size = 0;
            for (Node<K, V> node : oldData) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private void insertNode(Node<K, V> node) {
        int bucket = getBucketIndex(node.key);
        node.next = storage[bucket];
        storage[bucket] = node;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
