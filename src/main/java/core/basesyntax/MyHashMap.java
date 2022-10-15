package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int size;
    private Entry<K, V>[] table;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Entry[DEFAULT_INITIAL_CAPACITY];
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        Entry(K key, V value) {
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

            return Objects.equals(key, entry.key);
        }

        @Override
        public int hashCode() {
            return key != null ? key.hashCode() : 0;
        }
    }

    @Override
    public void put(K key, V value) {
        if ((size + 1) > (capacity * LOAD_FACTOR)) {
            resize();
        }
        int hash = getIndexByTheKey(key);
        Entry<K, V> e = table[hash];
        if (e == null) {
            table[hash] = new Entry<>(key, value);
            size++;
        } else {
            while (e.next != null) {
                if ((e.key != null) && (e.key.equals(key))) {
                    e.value = value;
                    return;
                }
                if (e.key == null && key == null) {
                    e.value = value;
                    return;
                }
                e = e.next;
            }
            if ((e.key != null) && (e.key.equals(key))) {
                e.value = value;
                return;
            }
            if (e.key == null && key == null) {
                e.value = value;
                return;
            }
            e.next = new Entry<>(key, value);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int hash = getIndexByTheKey(key);
        Entry<K, V> e = table[hash];
        if (e == null) {
            return null;
        }
        while (e != null) {
            if (key == null) {
                if (e.key == null) {
                    return e.value;
                }
            } else if (key.equals(e.key)) {
                return e.value;
            }
            e = e.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] newTable = new Entry[capacity * 2];
        for (Entry<K, V> e : table) {
            if (e != null) {
                while (e.next != null) {
                    transfer(e.key, e.value, newTable);
                    e = e.next;
                }
                transfer(e.key, e.value, newTable);
            }
        }
        table = newTable;
        capacity *= 2;
    }

    private void transfer(K key, V value, Entry<K, V>[] newTable) {
        int hash = (key == null) ? 0 : (key.hashCode() % (capacity * 2));
        hash = hash < 0 ? -hash : hash;
        Entry<K, V> e = newTable[hash];
        if (e == null) {
            newTable[hash] = new Entry<>(key, value);
        } else {
            while (e.next != null) {
                if ((e.key != null) && (e.key.equals(key))) {
                    e.value = value;
                    return;
                }
                if (e.key == null && key == null) {
                    e.value = value;
                    return;
                }
                e = e.next;
            }
            if ((e.key != null) && (e.key.equals(key))) {
                e.value = value;
                return;
            }
            if (e.key == null && key == null) {
                e.value = value;
                return;
            }
            e.next = new Entry<>(key, value);
        }
    }

    private int getIndexByTheKey(K key) {
        int hash = (key == null) ? 0 : (key.hashCode() % capacity);
        return hash < 0 ? -hash : hash;
    }
}
