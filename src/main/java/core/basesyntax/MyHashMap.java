package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Bucket<K, V>[] buckets = new Bucket[DEFAULT_CAPACITY];
    private int size;

    @Override
    public void put(K key, V value) {
        if (size > (int)(buckets.length * LOAD_FACTOR)) {
            increaseSize();
        }
        int bucketIdx = key != null ? (key.hashCode() >>> 1) % buckets.length : 0;
        Bucket<K, V> bucket = buckets[bucketIdx];
        if (bucket == null) {
            buckets[bucketIdx] = new Bucket<>(key, value);
            ++size;
            return;
        }
        do {
            if (Objects.equals(bucket.key, key)) {
                bucket.value = value;
                return;
            }
            if (bucket.next == null) {
                break;
            }
            bucket = bucket.next;
        } while (true);
        bucket.next = new Bucket<>(key, value);
        ++size;
    }

    private void increaseSize() {
        Bucket<K, V>[] oldBuckets = buckets;
        buckets = new Bucket[buckets.length << 1];
        size = 0;
        for (Bucket<K, V> bucket : oldBuckets) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int bucketIdx = key != null ? (key.hashCode() >>> 1) % buckets.length : 0;
        Bucket<K, V> bucket = buckets[bucketIdx];
        for (; bucket != null; bucket = bucket.next) {
            if (Objects.equals(bucket.key, key)) {
                return bucket.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Bucket<K, V> {
        private final K key;
        private V value;
        private Bucket<K, V> next;

        Bucket(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
