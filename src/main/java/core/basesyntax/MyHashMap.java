package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 32;
    private static final double LOAD_FACTOR = 0.75;

    private int size;
    private Entry<K, V>[] table;

    public MyHashMap() {
        this.table = new Entry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int index = getIndex(key);
        Entry<K, V> entry = table[index];

        if (entry == null) {
            table[index] = new Entry<>(key, value);
            size++;
        } else {
            while (entry.next != null) {
                if (Objects.equals(entry.key, key)) {
                    entry.value = value;
                    return;
                }
                entry = entry.next;
            }
            if (Objects.equals(entry.key, key)) {
                entry.value = value;
            } else {
                entry.next = new Entry<>(key, value);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = getIndex(key);
        Entry<K, V> entry = table[hash];
        while (entry != null) {
            if (Objects.equals(entry.key, key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resizeIfNeeded() {
        double currentLoadFactor = (double) size / table.length;
        if (currentLoadFactor > LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Entry<K, V>[] oldTable = table;
        size = 0;
        table = new Entry[newCapacity];

        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
