package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static float threshold;

    private Bucket<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Bucket[DEFAULT_CAPACITY];
        threshold = DEFAULT_CAPACITY * LOAD_FACTOR;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            increaseSize();
        }
        int bucketIndex = getBucketIndex(key, buckets.length);
        Bucket<K, V> bucket = getBucket(key, bucketIndex);
        if (bucket != null) {
            bucket.value = value;
        } else {
            Bucket<K, V> bucketEntry = new Bucket<>(key, value, buckets[bucketIndex]);
            buckets[bucketIndex] = bucketEntry;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Bucket<K, V> bucket = getBucket(key, getBucketIndex(key, buckets.length));
        return bucket != null ? bucket.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucketIndex(K key, int capacity) {
        return key != null ? (key.hashCode() >>> 1) % buckets.length : 0;
    }

    private Bucket getBucket(K key, int index) {
        Bucket<K, V> bucket = buckets[index];
        while (bucket != null) {
            if (key == bucket.key || key != null && key.equals(bucket.key)) {
                return bucket;
            }
            bucket = bucket.next;
        }
        return null;
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

    private static class Bucket<K, V> {
        private final K key;
        private V value;
        private Bucket<K, V> next;

        Bucket(K key, V value, Bucket<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
