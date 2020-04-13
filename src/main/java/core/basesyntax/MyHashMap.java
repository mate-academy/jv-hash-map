package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFOULT_CAPASITY = 16;
    static final double LOAD_FACTOR = 0.75;
    private Entry<K, V>[] table;
    private int size;

    public <K, V> MyHashMap() {
        table = new Entry[DEFOULT_CAPASITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        Entry<K, V> presentInEntry = findEntry(key, indexForEntry(key));
        if (presentInEntry == null) {
            table[indexForEntry(key)] = new Entry(key, value, table[indexForEntry(key)]);
            size++;
            resize();
        } else {
            presentInEntry.value = value;
            return;
        }
    }

    private Entry findEntry(K key, int entryIndex) {
        Entry<K, V> entry = table[entryIndex];
        while (entry != null) {
            if (Objects.equals(key, entry.key)) {
                return entry;
            }
            entry = entry.next;
        }
        return null;
    }

    @Override
    public V getValue(K key) {
        Entry<K, V> entry = findEntry(key, indexForEntry(key));
        return entry == null ? null : entry.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size > table.length * LOAD_FACTOR) {
            Entry<K, V>[] newTable = new Entry[table.length * 2];
            size = 0;
            Entry<K, V>[] oldTable = table;
            table = newTable;
            for (Entry<K, V> entry : oldTable) {
                while (entry != null) {
                    put(entry.key, entry.value);
                    entry = entry.next;
                }
            }
        }
    }

    public int indexForEntry(K kay) {
        return kay == null ? 0 : Math.abs(kay.hashCode() % table.length);
    }

    private class Entry<K, V> {
        Entry<K, V> next;
        K key;
        V value;

        private Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
