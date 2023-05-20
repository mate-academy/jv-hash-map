package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_NUMBER_OF_BUCKETS = 16;
    static final float DEFAULT_LOADER_FACTOR = 0.75f;
    static final int INCREASE_RATE = 2;
    private int numberOfBuckets = DEFAULT_NUMBER_OF_BUCKETS;
    private int size;
    private Node<K,V>[] buckets;

    public MyHashMap() {
        this.buckets = new Node[DEFAULT_NUMBER_OF_BUCKETS];
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= buckets.length * DEFAULT_LOADER_FACTOR) {
            resize();
        }
        int hashCode = hashCode(key);
        int index = hashCode % buckets.length;
        Node<K, V> currentNode = buckets[index];
        if (buckets[index] == null) {
            buckets[index] = new Node(hashCode, key, value, null);
        } else {
            while ((currentNode.key != key && !currentNode.key.equals(key))
                    && currentNode.next != null) {
                currentNode = currentNode.next;
            }
            if (currentNode.key == key || currentNode.key.equals(key)) {
                currentNode.value = value;
                return;
            }
            currentNode.next = new Node(hashCode, key, value, null);
        }
        size++;
    }

    private int hashCode(K key) {
        return key == null ? 0 : Math.abs(Objects.hash(key));
    }

    @Override
    public V getValue(K key) {
        int index = hashCode(key) % buckets.length;
        Node<K, V> currentNode = buckets[index];
        while (currentNode != null) {
            if (currentNode.key == key || key != null && key.equals(currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V> currentNode;
        numberOfBuckets = buckets.length * INCREASE_RATE;
        Node[] rewriteBuckets = buckets;
        buckets = new Node[numberOfBuckets];
        size = 0;
        for (Node<K, V> oneBucket:rewriteBuckets) {
            currentNode = oneBucket;
            while (oneBucket != null) {
                put(oneBucket.key, oneBucket.value);
                oneBucket = oneBucket.next;
            }
        }
    }
}

