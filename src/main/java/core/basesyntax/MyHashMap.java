package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75d;
    private int currentCapacity = DEFAULT_CAPACITY;
    private int size;
    private Entry[] tables;

    public MyHashMap() {
        tables = new Entry[currentCapacity];
        size = 0;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % tables.length);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (key == null) {
            putForNullKey(value);
            return;
        }
        if (tables[index] == null) {
            tables[index] = new Entry<K, V>(key, value);
            size++;
            return;
        }
        Entry<K, V> entry = tables[index];
        while (entry != null) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            } else {
                entry = entry.next;
            }
        }
        addEntry(key, value, index);
        resize();
    }

    private void putForNullKey(V value) {
        Entry<K, V> entry = tables[0];
        while (entry != null) {
            if (entry.key == null) {
                entry.value = value;
                return;
            } else {
                entry = entry.next;
            }
        }
        addEntry(null, value, getIndex(null));
        resize();
    }

    private void addEntry(K key, V value, int index) {
        Entry<K, V> entry = new Entry<>(key, value);
        entry.next = tables[index];
        tables[index] = entry;
        size++;
    }

    private void resize() {
        if (size >= currentCapacity * LOAD_FACTOR) {
            currentCapacity = currentCapacity * 2;
            size = 0;
            Entry<K, V>[] prevTable = tables;
            tables = new Entry[currentCapacity];
            for (int i = 0; i < prevTable.length; i++) {
                Entry<K, V> entry = prevTable[i];
                while (entry != null) {
                    put(entry.key, entry.value);
                    entry = entry.next;
                }
            }
        }
    }


    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (key == null) {
            return tables[0] == null ? null : (V) tables[0].value;
        }
        Entry entry = tables[index];
        while (entry != null) {
            if (entry.key.equals(key)) {
                return (V) entry.value;
            } else {
                entry = entry.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Entry<?, ?> entry = (Entry<?, ?>) o;
            return Objects.equals(key, entry.key)
                    && Objects.equals(value, entry.value)
                    && Objects.equals(next, entry.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }
}
