package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int INCREASING_NUM = 2;
    private static final int ZERO_HASH = 0;

    private LinkedList<Entry<K, V>>[] table;
    private int size;

    public MyHashMap() {
        table = new LinkedList[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putNullKey(value);
            return;
        }

        int hash = key.hashCode();
        int index = getIndex(hash);

        if (table[index] == null) {
            table[index] = new LinkedList<>();
            table[index].add(new Entry<>(key, value));
            size++;
            return;
        }

        for (Entry<K, V> entry : table[index]) {
            if (entry.key != null && entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        table[index].add(new Entry<>(key, value));
        size++;

        if ((double) size / table.length > LOAD_FACTOR) {
            resizeTable();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getKeyNullVal();
        }

        int hash = key.hashCode();
        int index = getIndex(hash);

        if (table[index] != null) {
            for (Entry<K, V> entry : table[index]) {
                if (entry.key != null && entry.key.equals(key)) {
                    return entry.value;
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
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int getIndex(int hash) {
        return (hash == ZERO_HASH) ? ZERO_HASH : Math.abs(hash % table.length);
    }

    private int getIndex(int hash, int tableSize) {
        return Math.abs(hash % tableSize);
    }

    private void resizeTable() {
        int newCapacity = table.length * INCREASING_NUM;
        LinkedList<Entry<K, V>>[] newTable = new LinkedList[newCapacity];

        for (LinkedList<Entry<K, V>> bucket : table) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    int newHash = entry.key.hashCode();
                    int newIndex = getIndex(newHash, newCapacity);

                    if (newTable[newIndex] == null) {
                        newTable[newIndex] = new LinkedList<>();
                    }

                    newTable[newIndex].add(entry);
                }
            }
        }
        table = newTable;
    }

    private void putNullKey(V value) {
        int index = getIndex(ZERO_HASH);

        if (table[index] == null) {
            table[index] = new LinkedList<>();
            table[index].add(new Entry<>(null, value));
            size++;
            if ((double) size / table.length > LOAD_FACTOR) {
                resizeTable();
            }
            return;
        }

        for (Entry<K, V> entry : table[index]) {
            if (entry.key == null) {
                entry.value = value;
                return;
            }
        }

        table[index].add(new Entry<>(null, value));
        size++;
        if ((double) size / table.length > LOAD_FACTOR) {
            resizeTable();
        }
    }

    private V getKeyNullVal() {
        if (table[0] != null) {
            for (Entry<K, V> entry : table[0]) {
                if (entry.key == null) {
                    return entry.value;
                }
            }
        }
        return null;
    }
}
