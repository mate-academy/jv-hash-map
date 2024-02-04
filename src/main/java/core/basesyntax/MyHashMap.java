package core.basesyntax;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private LinkedList<Entry<K, V>>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new LinkedList[DEFAULT_CAPACITY];
        size = 0;
    }

    public void put(K key, V value) {
        int index = getIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];
        if (bucket == null) {
            bucket = new LinkedList<>();
            buckets[index] = bucket;
        }

        for (Entry<K, V> entry : bucket) {
            if (Objects.equals(entry.getKey(), key)) {
                entry.setValue(value);
                return;
            }
        }

        bucket.add(new AbstractMap.SimpleEntry<>(key, value));
        size++;

        if ((double) size / buckets.length >= LOAD_FACTOR) {
            resize();
        }
    }

    public V getValue(K key) {
        int index = getIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];
        if (bucket == null) {
            return null;
        }

        for (Entry<K, V> entry : bucket) {
            if (Objects.equals(entry.getKey(), key)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyHashMap<?, ?> myHashMap = (MyHashMap<?, ?>) o;
        return size == myHashMap.size && Arrays.equals(buckets, myHashMap.buckets);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(buckets);
        return result;
    }

    @Override
    public String toString() {
        return "MyHashMap{" + "buckets=" + Arrays.toString(buckets) + ", size=" + size + '}';
    }

    private int getIndex(K key) {
        return getIndex(key, buckets.length);
    }

    private int getIndex(K key, int length) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % length;
    }

    private void resize() {
        LinkedList<Entry<K, V>>[] newBuckets = new LinkedList[buckets.length * 2];
        for (LinkedList<Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    int index = getIndex(entry.getKey(), newBuckets.length);
                    LinkedList<Entry<K, V>> newBucket = newBuckets[index];
                    if (newBucket == null) {
                        newBucket = new LinkedList<>();
                        newBuckets[index] = newBucket;
                    }
                    newBucket.add(entry);
                }
            }
        }
        buckets = newBuckets;
    }
}
