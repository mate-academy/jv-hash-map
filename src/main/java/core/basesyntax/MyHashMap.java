package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MULTIPLIER = 2;
    private Entry<K, V>[] table = new Entry[INITIAL_CAPACITY];
    private int size;
    private int threshold;

    public MyHashMap() {
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = indexPosition(key);
        Entry<K, V> entry = table[index];
        while (entry != null) {
            if (Objects.equals(entry.key, key)) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }
        addEntry(key, value, index);
    }

    @Override
    public V getValue(K key) {
        int index = indexPosition(key);
        Entry<K, V> entry = table[index];
        while (entry != null) {
            if (Objects.equals(key, entry.key)) {
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

    private void addEntry(K key, V value, int index) {
        Entry<K, V> entry = table[index];
        table[index] = new Entry<>(key, value, entry);
        size++;
    }

    private void resize() {
        threshold = (int) (table.length * LOAD_FACTOR);
        if (size == threshold) {
            Entry<K, V>[] newTable = table;
            table = new Entry[table.length * MULTIPLIER];
            size = 0;
            for (Entry<K, V> entry : newTable) {
                while (entry != null) {
                    put(entry.key, entry.value);
                    entry = entry.next;
                }
            }
        }
    }

    private int indexPosition(K key) {
        int hash = key == null ? 0 : key.hashCode();
        return Math.abs(hash) % table.length;
    }
}
