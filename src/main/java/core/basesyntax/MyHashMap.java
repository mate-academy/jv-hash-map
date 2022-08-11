package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MAGNIFICATION_FACTOR = 2;

    private Bucket<K, V>[] table;
    private int size;
    private int capacity;
    private int threshold;

    public MyHashMap() {
        table = new Bucket[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            increaseCapacity();
        }
        int index = getBucketIndex(key);
        Bucket<K, V> bucket = table[index];

        if (bucket == null) {
            table[index] = new Bucket<>(key, value);
        } else {
            while (bucket.next != null) {
                if (Objects.equals(bucket.key, key)) {
                    bucket.value = value;
                    return;
                }
                bucket = bucket.next;
            }
            if (Objects.equals(bucket.key, key)) {
                bucket.value = value;
                return;
            }
            bucket.next = new Bucket<>(key, value);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        Bucket<K, V> bucket = table[index];
        while (bucket != null) {
            if (Objects.equals(bucket.key, key)) {
                return bucket.value;
            }
            bucket = bucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void increaseCapacity() {
        capacity *= MAGNIFICATION_FACTOR;
        Bucket<K, V>[] oldTable = table;
        table = new Bucket[capacity];
        size = 0;
        for (Bucket<K, V> bucket : oldTable) {
            Bucket<K, V> current = bucket;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private class Bucket<K, V> {
        private final K key;
        private V value;
        private Bucket<K, V> next;

        public Bucket(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
