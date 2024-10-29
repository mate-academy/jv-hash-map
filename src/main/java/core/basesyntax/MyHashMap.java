package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = createBucketArray(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    private Node<K, V>[] createBucketArray(int capacity) {
        return new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int index = determineIndex(key);

        // Handle the case where key is null and should be stored in bucket[0]
        if (key == null) {
            if (buckets[0] == null) {
                buckets[0] = new Node<>(null, value);
                size++;
            } else {
                insertOrUpdateInBucket(buckets[0], key, value);
            }
            return;
        }

        if (buckets[index] == null) {
            buckets[index] = new Node<>(key, value);
            size++;
        } else {
            insertOrUpdateInBucket(buckets[index], key, value);
        }
    }

    private void insertOrUpdateInBucket(Node<K, V> head, K key, V value) {
        Node<K, V> current = head;

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Node<>(key, value);
                size++;
                return;
            }
            current = current.next;
        }
    }

    private void resizeIfNeeded() {
        if (size >= buckets.length * LOAD_FACTOR) {
            Node<K, V>[] oldBuckets = buckets;
            buckets = createBucketArray(buckets.length * 2);
            size = 0;

            for (Node<K, V> node : oldBuckets) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    private Node<K, V> getNode(K key) {
        int index = determineIndex(key);

        Node<K, V> node = buckets[index];
        while (node != null) {
            if (key == null && node.key == null || key != null && key.equals(node.key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int determineIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() & (buckets.length - 1));
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
