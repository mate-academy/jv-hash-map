package core.basesyntax;

import java.util.Objects;
/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPASITY = 16;
    private static final float LOAD_FACTOR = 0.35f;
    private Bucket[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Bucket[DEFAULT_CAPASITY];
    }

    public MyHashMap(int initialCapacity) {
        buckets = new Bucket[initialCapacity];
    }

    private class Bucket<K, V> {
        private K key;
        private V value;

        private Bucket(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int findIndexByHash(K key) {
        return Math.abs(hash(key) % buckets.length);
    }

    @Override
    public void put(K key, V value) {
        if (size >= buckets.length * LOAD_FACTOR) {
            resizeHashMap();
        }
        int index = findIndexByHash(key);
        while (true) {
            if (index == buckets.length - 1) {
                index = 0;
            }
            if (buckets[index] == null) {
                buckets[index] = new Bucket(key, value);
                size++;
                return;
            } else {
                if (Objects.equals(buckets[index].key, key)) {
                    buckets[index].value = value;
                    return;
                }
            }
            index++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = findIndexByHash(key);
        for (int i = 0; i < buckets.length - 1; i++) {
            if (buckets[index] != null && Objects.equals(buckets[index].key, key)) {
                return (V) buckets[index].value;
            }
            if (index == buckets.length - 1) {
                return null;
            }
            index++;
        }
        return null;
    }

    private void resizeHashMap() {
        size = 0;
        Bucket<K, V>[] copyOfBuckets = buckets;
        buckets = new Bucket[buckets.length * 2];
        for (int i = 0; i < copyOfBuckets.length; i++) {
            if (copyOfBuckets[i] != null) {
                put(copyOfBuckets[i].key, copyOfBuckets[i].value);
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        String sout = "";
        for (Bucket b : buckets) {
            sout += b.value + "\n";
        }
        return sout;
    }
}
