package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int SCALE = 2;
    private int size;
    private Entry<K, V>[] table;

    public MyHashMap() {
        table = new Entry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        grow();
        int index = getIndex(key);
        Entry<K, V> entry = table[index];
        if (entry == null) {
            table[index] = new Entry<K, V>(key, value);
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
                return;
            }
            entry.next = new Entry<K, V>(key, value);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Entry<K, V> entry = table[index];
        if (entry == null) {
            return null;
        }
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

    private <K> int getIndex(K key) {
        int hash = key == null ? 0 : key.hashCode() % table.length;
        int index = hash >= 0 ? hash : -hash;
        return index;
    }

    private void grow() {
        int treshold = (int) (LOAD_FACTOR * table.length);
        if (size < treshold) {
            return;
        }
        int oldCapasity = table.length;
        int newCapasity = oldCapasity * SCALE;
        Entry<K, V>[] newTable = new Entry[newCapasity];
        Entry<K, V>[] entries = getArray();
        table = newTable;
        size = 0;
        for (Entry<K, V> entry : entries) {
            put(entry.key, entry.value);
        }
    }

    private Entry<K,V>[] getArray() {
        Entry<K, V>[] entries = new Entry[size];
        int i = 0;
        for (Entry<K, V> entry : table) {
            while (entry != null) {
                entries[i++] = entry;
                entry = entry.next;
            }
        }
        return entries;
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
