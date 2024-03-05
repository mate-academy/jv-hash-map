package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Entry<K, V>[] table;
    private int size;

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private final Entry<K, V> next;

        private Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        this.table = new Entry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= LOAD_FACTOR * table.length) {
            resize();
        }
        int index = hashIndex(key);
        Entry<K, V> entry = table[index];
        while (entry != null) {
            if (Objects.equals(entry.key, key)) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }
        Entry<K, V> node = table[index];
        add(index, key, value, node);
    }

    @Override
    public V getValue(K key) {
        int index = hashIndex(key);
        Entry<K, V> entry = table[index];
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

    private int hashIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        Entry<K, V>[] oldTable = table;
        table = new Entry[table.length * 2];
        size = 0;
        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    private void add(int index, K key, V value, Entry<K,V> entry) {
        table[index] = new Entry<>(key, value, entry);
        size++;
    }
}
