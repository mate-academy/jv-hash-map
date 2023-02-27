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
    public int getSize() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> existingNode = getNodeByKey(key);
        if (existingNode != null) {
            existingNode.value = value;
            return;
        }
        resize();
        insertNode(new Node<>(key, value), storage);
        size++;
    }

    @Override
    public V getValue(K key) {
        final Node<K, V> existingNode = getNodeByKey(key);
        return existingNode == null ? null : existingNode.value;
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

    private void resize() {
        if (size / (double) storage.length > RESIZE_FACTOR) {
            Node<K, V>[] oldData = storage;
            storage = (Node<K, V>[]) new Node[storage.length << 1];
            size = 0;
            for (Node<K, V> rootNode : oldData) {
                Node<K,V> current = rootNode;
                while (current != null) {
                    put(current.key, current.value);
                    current = current.next;
                }
            }
        }
    }

    private void insertNode(Node<K, V> node, Node<K, V>[] storage) {
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
