package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int defaultCapacity = 16;
    private static final double loadFactor = 0.75;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[defaultCapacity];
    }

    @Override
    public void put(K key, V value) {
        putValue(hash(key), key, value);
    }

    private void putValue(int hash, K key, V value) {
        if (size == buckets.length * loadFactor) {
            resize();
        }
        Node<K, V> putNode = new Node(key, value, null);
        int indexOfBucket = hash % buckets.length;
        if (buckets[indexOfBucket] == null) {
            buckets[indexOfBucket] = putNode;
            size++;
        } else {
            Node<K, V> currentNodeInBucket = buckets[indexOfBucket];
            while (currentNodeInBucket != null) {
                if (Objects.equals(currentNodeInBucket.key, putNode.key)) {
                    currentNodeInBucket.value = putNode.value;
                    return;
                }
                if (currentNodeInBucket.next == null) {
                    currentNodeInBucket.next = putNode;
                    size++;
                    return;
                }
                currentNodeInBucket = currentNodeInBucket.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode;
        for (Node<K, V> bucket : buckets) {
            if (bucket != null) {
                currentNode = bucket;
                while (currentNode != null) {
                    if (Objects.equals(currentNode.key, key)) {
                        return currentNode.value;
                    }
                    currentNode = currentNode.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[buckets.length << 1];
        size = 0;
        for (Node<K, V> oldBucket : oldBuckets) {
            while (oldBucket != null) {
                Node<K, V> currentNode = oldBucket;
                put(currentNode.key, currentNode.value);
                oldBucket = oldBucket.next;
            }
        }
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        int h = 31 * key.hashCode();
        return (h < 0) ? -h : 31 * key.hashCode();
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key) && Objects.equals(value, node.value);
        }

    }
}
