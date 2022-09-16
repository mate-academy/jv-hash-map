package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Entry<K, V>[] table;

    public MyHashMap() {
        this.table = (Entry<K, V>[]) new Entry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if ((float) size / table.length >= LOAD_FACTOR) {
            resize();
        }
        int keyHashcode = key != null ? key.hashCode() : 0;
        Entry<K, V> newEntry = new Entry<>(keyHashcode, key, value, null);
        int index = keyHashcode != 0 ? Math.abs(newEntry.hash) % table.length : 0;
        Entry<K, V> entry = table[index];
        if (entry != null) {
            while (true) {
                if (Objects.equals(entry.key, newEntry.key)) {
                    entry.value = newEntry.value;
                    return;
                }
                if (entry.next != null) {
                    entry = entry.next;
                } else {
                    break;
                }
            }
            entry.next = newEntry;
        } else {
            table[index] = newEntry;
        }
        size++;
    }

    private void resize() {
        Entry<K, V>[] oldTable = (Entry<K, V>[]) new Entry[table.length];
        System.arraycopy(table, 0, oldTable, 0, table.length);
        int newSize = table.length << 1;
        table = (Entry<K, V>[]) new Entry[newSize];
        size = 0;
        for (final Entry<K, V> entry : oldTable) {
            if (entry != null) {
                put(entry.key, entry.value);
                Entry<K, V> childEntry = entry.next;
                while (childEntry != null) {
                    put(childEntry.key, childEntry.value);
                    childEntry = childEntry.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = key != null ? Math.abs(key.hashCode()) % table.length : 0;
        Entry<K, V> entry = table[index];
        if (entry != null) {
            while (true) {
                if (Objects.equals(key, entry.key)) {
                    return entry.value;
                }
                if (entry.next != null) {
                    entry = entry.next;
                } else {
                    break;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Entry<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Entry<K, V> next;

        public Entry(final int hash, final K key, final V value, final Entry<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final Entry<?, ?> entry = (Entry<?, ?>) o;
            if (!Objects.equals(key, entry.key)) {
                return false;
            }
            return Objects.equals(value, entry.value);
        }

        @Override
        public int hashCode() {
            int result = key != null ? key.hashCode() : 0;
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
