package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Entry<K, V>[] table;
    private int size = 0;

    public MyHashMap() {
        table = new Entry[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if ((double) size / table.length >= LOAD_FACTOR) {
            resize();
        }
        int hash = hash(key);
        if (table[hash] == null) {
            table[hash] = new Entry<K, V>(key, value, null);
            size++;
            return;
        }
        Entry<K, V> entry = table[hash];
        while (entry != null) {
            if (Objects.equals(key, entry.key)) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }
        table[hash] = new Entry<K, V>(key, value, table[hash]);
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
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

    public boolean isEmpty() {
        return size == 0;
    }

    public V remove(K key) {
        int hash = hash(key);
        Entry<K, V> entry = table[hash];
        Entry<K, V> previousEntry = entry;
        int counterEntry = 0;
        while (entry != null) {
            counterEntry++;
            if (Objects.equals(entry.key, key)) {
                if (counterEntry == 1) {
                    table[hash] = entry.next;
                }
                previousEntry.next = entry.next;
                size--;
                return entry.value;
            }
            previousEntry = entry;
            entry = entry.next;
        }
        return null;
    }

    private void resize() {
        Entry<K, V>[] oldTable = table;
        table = new Entry[table.length * 2];
        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                int hash = hash(entry.key);
                if (table[hash] == null) {
                    table[hash] = new Entry<K, V>(entry.key, entry.value, null);
                    entry = entry.next;
                    continue;
                }
                table[hash] = new Entry<K, V>(entry.key, entry.value, table[hash]);
                entry = entry.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        private Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
