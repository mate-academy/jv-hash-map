package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT = 1 << 4;
    private static final float LOAD_FACTOR = 0.75f;
    private LinkedList<Entry<K,V>>[] buckets;
    private V null1;
    private int size;

    public MyHashMap() {
        buckets = new LinkedList[DEFAULT];
        for (int i = 0; i < DEFAULT; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    private static class Entry<K, V> {
        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            null1 = value;
            return;
        }
        int index = getBucketIndex(key);
        LinkedList<Entry<K,V>> bucket = buckets[index];
        for (Entry<K,V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        bucket.add(new Entry<>(key, value));
        size++;

        if (size > buckets.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return null1;
        }
        int index = getBucketIndex(key);
        LinkedList<Entry<K,V>> bucket = buckets[index];
        for (Entry<K,V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size + (null1 != null ? 1 : 0);
    }

    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode() % buckets.length);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        LinkedList<Entry<K,V>>[] oldbuckets = buckets;
        buckets = new LinkedList[oldbuckets.length * 2];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<>();
        }
        size = 0;
        for (LinkedList<Entry<K,V>> entry : oldbuckets) {
            for (Entry<K, V> entry1 : entry) {
                put(entry1.key, entry1.value);
            }
        }
    }
}
