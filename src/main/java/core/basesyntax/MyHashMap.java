package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private Entry<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Entry<K, V>[]) new Entry[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            if (table[0] == null) {
                table[0] = new Entry<>(null, value, null);
                size++;
            } else {
                Entry<K, V> e = table[0];
                while (e != null) {
                    if (e.key == null) {
                        e.value = value;
                        return;
                    }
                    e = e.next;
                }
                table[0] = new Entry<>(null, value, table[0]);
                size++;
            }
        } else {
            int hash = hash(key);
            int index = indexFor(hash, table.length);
            Entry<K, V> e = table[index];

            while (e != null) {
                if (Objects.equals(e.key, key)) {
                    e.value = value;
                    return;
                }
                e = e.next;
            }

            addEntry(hash, key, value, index);
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            Entry<K, V> e = table[0];
            while (e != null) {
                if (e.key == null) {
                    return e.value;
                }
                e = e.next;
            }
            return null;
        } else {
            int hash = hash(key);
            int index = indexFor(hash, table.length);
            Entry<K, V> e = table[index];

            while (e != null) {
                if (Objects.equals(e.key, key)) {
                    return e.value;
                }
                e = e.next;
            }
            return null;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addEntry(int hash, K key, V value, int index) {
        if (size >= table.length * LOAD_FACTOR) {
            resize(GROW_FACTOR * table.length);
            index = indexFor(hash, table.length); // Recalculate index after resizing
        }

        Entry<K, V> e = table[index];
        table[index] = new Entry<>(key, value, e);
        size++;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        table = (Entry<K, V>[]) new Entry[newCapacity];
        size = 0;

        for (Entry<K, V> e : oldTable) {
            while (e != null) {
                Entry<K, V> next = e.next;
                int index = indexFor(hash(e.key), newCapacity);
                e.next = table[index];
                table[index] = e;
                e = next;
                size++;
            }
        }
    }

    private int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int indexFor(int hash, int length) {
        return hash % length;
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
