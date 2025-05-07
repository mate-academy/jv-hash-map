package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        buckets = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K,V> element = getNode(key);
        resize();
        if (element == null) {
            int bucketNumber = getBucketNumber(key);
            buckets[bucketNumber] = new Node<>(key, value, buckets[bucketNumber]);
            size++;
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
        int bucketNumber = getBucketNumber(key);
        Node<K,V> currentNode = buckets[bucketNumber];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private final int hash(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()));
    }

    private final int getBucketNumber(K key) {
        return hash(key) % buckets.length;
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
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
