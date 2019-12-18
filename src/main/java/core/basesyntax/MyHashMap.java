package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Entry<K, V>[] table = new Entry[INITIAL_CAPACITY];
    private int size;

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

    private int indexOfPosition(int hash, int length) {
        return Math.abs(hash) % table.length;
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        if (key == null) {
            putNullKey(value);
            return;
        }
        int hash = (key.hashCode());
        int index = indexOfPosition(hash, table.length);
        for (Entry<K, V> bucket = table[index]; bucket != null; bucket = bucket.next) {
            if (bucket.hash == hash && key.equals(bucket.key)) {
                bucket.value = value;
                return;
            }
        }
        addEntry(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        int index;
        if (key == null) {
            index = 0;
        } else {
            int hash = key.hashCode();
            index = indexOfPosition(hash, table.length);
        }
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

    private void putNullKey(V value) {
        for (Entry<K, V> bucket = table[0]; bucket != null; bucket = bucket.next) {
            if (bucket.key == null) {
                bucket.value = value;
                return;
            }
        }
        addEntry(0, null, value, 0);
    }

    private void addEntry(int hash, K key, V value, int index) {
        Entry<K, V> bucket = table[index];
        table[index] = new Entry<K, V>(hash, key, value, bucket);
        size++;
    }

    private void resize() {
        Entry[] newTable = table;
        table = new Entry[table.length * 2];
        size = 0;
        for (Entry<K, V> bucket : newTable) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }
}
