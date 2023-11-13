package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private static final int ZERO_HASH = 0;

    private Entry<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Entry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            int index = getIndex(ZERO_HASH);

            Entry<K, V> current = table[index];
            while (current != null) {
                if (current.key == null) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }

            addEntry(index, null, value);
            return;
        }

        int index = getIndexFromKey(key);

        Entry<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        addEntry(index, key, value);
    }

    @Override
    public V getValue(K key) {
        int index = key == null ? 0 : getIndexFromKey(key);

        Entry<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
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
    }

    private int getIndex(int hash) {
        return (hash == ZERO_HASH) ? ZERO_HASH : Math.abs(hash % table.length);
    }

    private int getIndexFromKey(K key) {
        int hash = key.hashCode();
        return getIndex(hash);
    }

    private void addEntry(int index, K key, V value) {
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = table[index];
        table[index] = newEntry;
        size++;

        resizeIfNeeded();
    }

    private void resizeIfNeeded() {
        if ((double) size / table.length > LOAD_FACTOR) {
            int newCapacity = table.length * GROW_FACTOR;
            rehash(newCapacity);
        }
    }

    private void rehash(int newCapacity) {
        Entry<K, V>[] oldTable = table;

        table = (Entry<K, V>[]) new Entry[newCapacity];
        size = 0;

        for (Entry<K, V> kvEntry : oldTable) {
            Entry<K, V> current = kvEntry;
            while (current != null) {
                K key = current.key;
                V value = current.value;
                put(key, value);
                current = current.next;
            }
        }
    }
}
