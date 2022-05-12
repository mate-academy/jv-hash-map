package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private int size;
    private Node<K, V>[] buckets = new Node[DEFAULT_CAPACITY];

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(key, value, getHash(key));
        int currentBucketNumber = newNode.hash % buckets.length;
        if (buckets[currentBucketNumber] == null) {
            buckets[currentBucketNumber] = newNode;
        } else {
            Node<K, V> node = buckets[currentBucketNumber];
            while (node != null) {
                if (Objects.equals(node.key, newNode.key)) {
                    node.value = newNode.value;
                    return;
                }
                if (node.next == null) {
                    node.next = newNode;
                    break;
                }
                node = node.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        Node<K, V> currentBucket = buckets[hash % buckets.length];
        while (currentBucket != null) {
            if (Objects.equals(currentBucket.key, key)) {
                return currentBucket.value;
            }
            currentBucket = currentBucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, int hash) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private void resize() {
        if (size > buckets.length * LOAD_FACTOR) {
            Node<K, V>[] oldBuckets = buckets;
            buckets = new Node[oldBuckets.length * 2];
            size = 0;
            for (int i = 0; i < oldBuckets.length; i++) {
                Node<K, V> node = oldBuckets[i];
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getHash(K key) {
        int hash = (key == null ? 0 : key.hashCode() < 0 ? key.hashCode() * -1 : key.hashCode());
        return hash;
    }
}
