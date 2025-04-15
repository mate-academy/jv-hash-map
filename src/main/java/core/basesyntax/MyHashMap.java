package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Entry<K, V>[] table;
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Entry<K, V>[]) new Entry[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = indexFor(key, table.length);
        Entry<K, V> head = table[index];

        for (Entry<K, V> e = head; e != null; e = e.next) {
            if (Objects.equals(e.key, key)) {
                e.value = value;
                return;
            }
        }

        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = head;
        table[index] = newEntry;
        size++;

        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = indexFor(key, table.length);
        Entry<K, V> e = table[index];

        while (e != null) {
            if (Objects.equals(e.key, key)) {
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

    private int indexFor(K key, int length) {
        return (key == null) ? 0 : (key.hashCode() & 0x7FFFFFFF) % length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Entry<K, V>[] newTable = (Entry<K, V>[]) new Entry[newCapacity];

        for (Entry<K, V> e : table) {
            while (e != null) {
                Entry<K, V> next = e.next;
                int index = indexFor(e.key, newCapacity);

                e.next = newTable[index];
                newTable[index] = e;

                e = next;
            }
        }

        table = newTable;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }
}
