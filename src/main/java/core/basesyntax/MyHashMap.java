package core.basesyntax;

import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int bucketIndex = getBucketIndex(hash);
        Node<K, V> bucket = table[bucketIndex];
        if (bucket == null) {
            table[bucketIndex] = new Node<>(hash, key, value, null);
        } else {
            Node<K, V> prev = null;
            while (bucket != null) {
                if (
                        bucket.hash == hash
                        && (bucket.key == key || (bucket.key != null && bucket.key.equals(key)))
                ) {
                    bucket.value = value;
                    return;
                }
                prev = bucket;
                bucket = bucket.next;
            }
            prev.next = new Node<>(hash, key, value, null);
        }

        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node<K, V> bucket = table[getBucketIndex(hash)];
        while (bucket != null) {
            if (
                    bucket.hash == hash
                    && (bucket.key == key || (key != null && key.equals(bucket.key)))
            ) {
                return bucket.value;
            }
            bucket = bucket.next;
        }
        return null;
    }

    @Override
    public V remove(K key) {
        if (!isEmpty()) {
            int hash = hash(key);
            int bucketIndex = getBucketIndex(hash);
            Node<K, V> prev = null;
            Node<K, V> bucket = table[bucketIndex];
            while (bucket != null) {
                if (
                        bucket.hash == hash
                        && (bucket.key == key || (key != null && key.equals(bucket.key)))
                ) {
                    if (prev == null) {
                        table[bucketIndex] = bucket.next;
                    } else {
                        prev.next = bucket.next;
                    }
                    bucket.next = null;
                    --size;
                    return bucket.value;
                }
                prev = bucket;
                bucket = bucket.next;
            }
        }
        return null;
    }

    @Override
    public List<K> getKeys() {
        K[] keys = (K[]) new Object[size];
        for (int i = 0, j = 0; i < table.length; i++) {
            Node<K, V> bucket = table[i];
            while (bucket != null) {
                keys[j++] = bucket.key;
                bucket = bucket.next;
            }
        }
        return List.of(keys);
    }

    @Override
    public List<V> getValues() {
        V[] keys = (V[]) new Object[size];
        for (int i = 0, j = 0; i < table.length; i++) {
            Node<K, V> bucket = table[i];
            while (bucket != null) {
                keys[j++] = bucket.value;
                bucket = bucket.next;
            }
        }
        return List.of(keys);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getBucketIndex(int hash) {
        return table.length - 1 & hash;
    }

    /**
     * Because we are using power-of-two expansion,
     * the elements from each bucket must either stay at same index,
     * or move with a power of two offset in the new table.
     */
    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> bucket = oldTable[i];
            if (bucket != null) {
                if (bucket.next == null) {
                    table[getBucketIndex(bucket.hash)] = bucket;
                } else {
                    Node<K, V> sameIndexBucketHead = null;
                    Node<K, V> sameIndexBucketTail = null;
                    Node<K, V> offsetBucketHead = null;
                    Node<K, V> offsetBucketTail = null;
                    while (bucket != null) {
                        if ((bucket.hash & oldTable.length) == 0) {
                            if (sameIndexBucketTail == null) {
                                sameIndexBucketHead = bucket;
                            } else {
                                sameIndexBucketTail.next = bucket;
                            }
                            sameIndexBucketTail = bucket;
                        } else {
                            if (offsetBucketTail == null) {
                                offsetBucketHead = bucket;
                            } else {
                                offsetBucketTail.next = bucket;
                            }
                            offsetBucketTail = bucket;
                        }
                        bucket = bucket.next;
                    }
                    if (sameIndexBucketTail != null) {
                        sameIndexBucketTail.next = null;
                        table[i] = sameIndexBucketHead;
                    }
                    if (offsetBucketTail != null) {
                        offsetBucketTail.next = null;
                        table[i + oldTable.length] = offsetBucketHead;
                    }
                }
            }
        }
    }

    private static final class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
