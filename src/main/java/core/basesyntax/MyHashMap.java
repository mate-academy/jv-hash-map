package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_SIZE = 16;
    public static final double RESIZE_FACTOR = 0.75;
    private Node<K, V>[] storage;
    private int size;

    public MyHashMap() {
        storage = (Node<K, V>[]) new Node[DEFAULT_SIZE];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> existingNode = getNodeAt(key);
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
        final Node<K, V> existingNode = getNodeAt(key);
        if (existingNode == null) {
            return null;
        }
        return existingNode.value;
    }

    private Node<K, V> getNodeAt(K key) {
        Node<K, V> starterNode = storage[getBucket(key)];
        if (starterNode == null) {
            return null;
        }
        for (Node<K, V> current = starterNode; current != null; current = current.next) {
            if (Objects.equals(current.key, key)) {
                return current;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucket(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % storage.length;
    }

    private void resize() {
        if (size / (double) storage.length > RESIZE_FACTOR) {
            Node<K, V>[] oldData = storage;
            storage = (Node<K, V>[]) new Node[storage.length << 1];
            for (Node<K, V> rootNode : oldData) {
                for (Node<K, V> innerNode = rootNode;
                        innerNode != null;
                        innerNode = innerNode.next) {
                    insertNode(innerNode.clone(), storage);
                }
            }
        }
    }

    private void insertNode(Node<K, V> node, Node<K, V>[] storage) {
        int bucket = getBucket(node.key);
        node.next = storage[bucket];
        storage[bucket] = node;
    }

    private static class Node<K, V> implements Cloneable {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        protected Node<K, V> clone() {
            return new Node<K, V>(key, value);
        }
    }
}
