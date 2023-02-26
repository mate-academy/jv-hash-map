package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size;
    private Node[] buckets;

    public MyHashMap() {
        buckets = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K,V> element = getNode(key);
        if (element == null) {
            int keyHash = hash(key);
            int bucketNumber = getBucketNumber(keyHash);
            buckets[bucketNumber] = new Node<>(keyHash, key, value, buckets[bucketNumber]);
            size++;
            resize();
        } else {
            element.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> element;
        return (element = getNode(key)) == null ? null : element.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNode(K key) {
        int keyHash = hash(key);
        int bucketNumber = getBucketNumber(keyHash);
        Node<K,V> currentNode = buckets[bucketNumber];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private final int hash(Object key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()));
    }

    private final int getBucketNumber(int hash) {
        return hash % buckets.length;
    }

    private void resize() {
        int arrayLength = buckets.length;
        if (size >= arrayLength * DEFAULT_LOAD_FACTOR) {
            size = 0;
            Node<K,V>[] oldNode = buckets;
            buckets = new Node[arrayLength * 2];
            for (Node<K,V> currentNode : oldNode) {
                while (currentNode != null) {
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
