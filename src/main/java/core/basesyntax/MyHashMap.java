
package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_INDEX = 2;
    private Node [] buckets = new Node[DEFAULT_CAPACITY];
    private int size;

    @Override
    public void put(K key, V value) {
        int hash = getHashCode(key);
        if (size > buckets.length * LOAD_FACTOR) {
            resize();
        }
        int index = hash % buckets.length;
        if (buckets[index] == null) {
            buckets[index] = new Node(hash, key, value, null);
        } else {
            Node currentBucket = buckets[index];
            while (currentBucket.next != null) {
                if (currentBucket.hash == hash
                        && Objects.equals(key, currentBucket.key)) {
                    currentBucket.value = value;
                    return;
                }
                currentBucket = currentBucket.next;
            }
            if (currentBucket.next == null) {
                if (currentBucket.hash == hash
                        && Objects.equals(key, currentBucket.key)) {
                    currentBucket.value = value;
                    return;
                }
            }
            Node newNode = new Node<>(hash, key, value, null);
            currentBucket.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getHashCode(key) % buckets.length;
        Node currentBucket = buckets[index];
        while (currentBucket != null) {
            if (Objects.equals(key, currentBucket.key)) {
                return (V) currentBucket.value;
            }
            currentBucket = currentBucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
    
    private class Node<K, V> {
        private int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[oldBuckets.length * RESIZE_INDEX];
        size = 0;
        for (int i = 0; i < oldBuckets.length; i++) {
            Node<K, V> node = oldBuckets[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getHashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }
}
