package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MULTIPLIER = 2;
    private Entry<K, V>[] table = new Entry[INITIAL_CAPACITY];
    private int size;
    private  int threshold;

    public MyHashMap() {
    }

    private static class Entry<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Entry<K, V> next;

        Entry(int hash, K key, V value, Entry<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = ourHashCode(key);
        int index = indexOfPosition(hash, table.length);
        Entry<K, V> bucket = table[index];
        if (bucket != null) {
            while (bucket != null) {
                if (bucket.hash == hash && Objects.equals(bucket.key, key)) {
                    bucket.value = value;
                    return;
                }
                bucket = bucket.next;
            }
        }
        addEntry(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        int index = indexOfPosition(ourHashCode(key),table.length);
        Entry<K, V> bucket = table[index];
        while (bucket != null) {
            if (Objects.equals(key, bucket.key)) {
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

    private void addEntry(int hash, K key, V value, int index) {
        Entry<K, V> bucket = table[index];
        table[index] = new Entry<>(hash, key, value, bucket);
        size++;
    }

    private void resize() {
        threshold = (int) (table.length * LOAD_FACTOR);
        if (size == threshold) {
            Entry<K, V>[] newTable = table;
            table = new Entry[table.length * MULTIPLIER];
            size = 0;
            for (Entry<K, V> bucket : newTable) {
                while (bucket != null) {
                    put(bucket.key, bucket.value);
                    bucket = bucket.next;
                }
            }
        }
    }

    private int ourHashCode(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int indexOfPosition(int hash, int length) {
        return Math.abs(hash) % length;
    }
}
