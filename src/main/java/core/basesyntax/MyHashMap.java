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
    private int greatestCommonDivisor;
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
        int x = 0;
        while (true) {
            int index = Math.abs((hash(key) + 2 * x++) % buckets.length);
            if (buckets[index] == null) {
                return index;
            } else {
                if (Objects.equals(buckets[index].key, key)) {
                    return index;
                }
            }

        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= buckets.length * LOAD_FACTOR) {
            resizeHashMap();
        }
        int index = findIndexByHash(key);
        if (buckets[index] == null) {
            buckets[index] = new Bucket(key, value);
            size++;
        } else {
            buckets[index].value = value;
        }
    }

    @Override
    public V getValue(K key) {
        int index = findIndexByHash(key);
        if (buckets[index] != null) {
            return (V) buckets[index].value;
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
