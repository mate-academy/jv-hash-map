package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private LinkedList<Entry<K, V>>[] table;
    private int size = 0;

    public MyHashMap() {
        table = new LinkedList[DEFAULT_CAPACITY];
    }

    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = indexFor(key.hashCode());
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }

        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                entry.value = value;  // Update value if key exists
                return;
            }
        }

        table[index].add(new Entry<>(key, value));
        size++;

        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = indexFor(key.hashCode());
        if (table[index] != null) {
            for (Entry<K, V> entry : table[index]) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }
        return null; // Key not found
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        LinkedList<Entry<K, V>>[] newTable = new LinkedList[table.length * 2];

        for (LinkedList<Entry<K, V>> bucket : table) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    int newIndex = indexFor(entry.key.hashCode(), newTable.length);
                    if (newTable[newIndex] == null) {
                        newTable[newIndex] = new LinkedList<>();
                    }
                    newTable[newIndex].add(entry);
                }
            }
        }

        table = newTable;
    }

    private int indexFor(int hashCode) {
        return indexFor(hashCode, table.length);
    }

    private int indexFor(int hashCode, int length) {
        return Math.abs(hashCode) % length;
    }
}
