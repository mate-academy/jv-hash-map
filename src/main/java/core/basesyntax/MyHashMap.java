package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75F;
    private int size;
    private Entry<K, V>[] table;

    public MyHashMap() {
        table = new Entry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        Entry<K, V> currentEntry = table[index];
        while (currentEntry != null) {
            if (Objects.equals(currentEntry.key, key)) {
                currentEntry.value = value;
                return;
            }
            currentEntry = currentEntry.next;
        }
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = table[index];
        table[index] = newEntry;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Entry<K, V> currentEntry = table[index];
        while (currentEntry != null) {
            if (Objects.equals(currentEntry.key, key)) {
                break;
            }
            currentEntry = currentEntry.next;
        }
        return currentEntry == null ? null : currentEntry.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        if (key != null) {
            return Math.abs(key.hashCode()) % table.length;
        }
        return 0;
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

    private static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
