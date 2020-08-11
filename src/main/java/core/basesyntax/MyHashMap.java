package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Entry<K, V>[] table;

    public MyHashMap() {
        table = (Entry<K, V>[]) new Entry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        putEntry(table, new Entry<>(key, value, null));
    }

    @Override
    public V getValue(K key) {
        Entry<K, V> entry = table[findIndex(table, key)];
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

    private int findIndex(Entry<K, V>[] table, K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        if (size + 1 >= table.length * LOAD_FACTOR) {
            size = 0;
            Entry<K, V>[] newTable = (Entry<K, V>[]) new Entry[table.length * 2];
            for (Entry<K, V> entry : table) {
                if (entry != null) {
                    Entry<K, V> nextEntry = entry.next;
                    while (nextEntry != null) {
                        Entry<K, V> tmpEntry = nextEntry.next;
                        nextEntry.next = null;
                        putEntry(newTable, nextEntry);
                        nextEntry = tmpEntry;
                    }
                    entry.next = null;
                    putEntry(newTable, entry);
                }
            }
            table = newTable;
        }
    }

    private void putEntry(Entry<K, V>[] table, Entry<K, V> entry) {
        int index = findIndex(table, entry.key);
        Entry<K, V> currentEntry = table[index];
        if (table[index] == null) {
            table[index] = entry;
            size++;
            return;
        }
        while (currentEntry != null) {
            if (Objects.equals(currentEntry.key, entry.key)) {
                currentEntry.value = entry.value;
                return;
            }
            if (currentEntry.next == null) {
                currentEntry.next = entry;
                size++;
                return;
            }
            currentEntry = currentEntry.next;
        }
    }

    private static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

}
