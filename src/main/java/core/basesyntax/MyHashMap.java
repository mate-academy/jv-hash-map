package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPASITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Bucket[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Bucket[DEFAULT_CAPASITY];
    }

    public MyHashMap(int initialCapacity) {
        buckets = new Bucket[initialCapacity];
    }

    private class Bucket<K, V> {
        private Bucket next;
        private K key;
        private V value;

        private Bucket(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int findIndexByHash(K key) {
        return key == null ? 0
                : Math.abs(key.hashCode() % buckets.length);
    }

    @Override
    public void put(K key, V value) {
        if (size >= buckets.length * LOAD_FACTOR) {
            resizeHashMap();
        }
        int index = findIndexByHash(key);
        if (buckets[index] == null) {
            buckets[index] = new Bucket<>(key, value);
            size++;
        } else {
            Bucket<K, V> bucket = buckets[index];
            while (true) {
                if (Objects.equals(key, bucket.key)) {
                    bucket.value = value;
                    return;
                }
                if (bucket.next == null) {
                    bucket.next = new Bucket<>(key, value);
                    size++;
                    return;
                }
                bucket = bucket.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = findIndexByHash(key);
        if (buckets[index] != null) {
            Bucket<K, V> bucket = buckets[index];
            while (bucket != null) {
                if (Objects.equals(key, bucket.key)) {
                    return bucket.value;
                }
                bucket = bucket.next;
            }
        }
        return null;
    }

    private void resizeHashMap() {
        size = 0;
        Bucket[] copyOfBuckets = buckets;
        buckets = new Bucket[buckets.length * 2];
        for (int i = 0; i < copyOfBuckets.length; i++) {
            if (copyOfBuckets[i] != null) {
                Bucket<K, V> bucket = copyOfBuckets[i];
                while (bucket != null) {
                    put(bucket.key, bucket.value);
                    bucket = bucket.next;
                }
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }
}
