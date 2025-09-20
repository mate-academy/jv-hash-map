package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int INITIAL_CAPACITY = 16;
    static final double LOAD_FACTOR = 0.75;

    private static class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private LinkedList<Entry<K, V>>[] table = new LinkedList[INITIAL_CAPACITY];
    private int size = 0;
    private double threshold = INITIAL_CAPACITY * LOAD_FACTOR;

    @Override
    public V put(K key, V value) {
        if (key == null) {
            if (table[0] == null) {
                table[0] = new LinkedList<>();
            }
            for (var entry : table[0]) {
                if (entry.key == key) {
                    var oldValue = entry.value;
                    entry.value = value;
                    return oldValue;
                }
            }
            table[0].add(new Entry<>(key, value));
            size++;
            resize(size);
            return null;
        }
        int index = getIndex(key);
        if (table[index] != null) {
            for (var entry : table[index]) {
                if (entry.key == key || (entry.key != null && entry.key.equals(key))) {
                    var oldValue = entry.value;
                    entry.value = value;
                    return oldValue;
                }
            }
        } else {
            table[index] = new LinkedList<>();
        }
        table[index].add(new Entry<>(key, value));
        size++;
        resize(size);
        return null;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (table[index] != null) {
            for (var entry : table[index]) {
                if (entry.key == key || (entry.key != null && entry.key.equals(key))) {
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

    private void resize(int size) {
        if (size > threshold) {
            int newCapacity = table.length * 2;
            LinkedList<Entry<K, V>>[] newTable = new LinkedList[newCapacity];
            int index;
            for (LinkedList<Entry<K, V>> entries : table) {
                if (entries != null) {
                    for (var entry : entries) {
                        if (entry.key == null) {
                            index = 0;
                        } else {
                            index = (entry.key.hashCode() & 0x7fffffff) % newCapacity;
                        }
                        if (newTable[index] == null) {
                            newTable[index] = new LinkedList<>();
                        }
                        newTable[index].add(entry);
                    }
                }
            }
            table = newTable;
            threshold = (int)(newCapacity * LOAD_FACTOR);
        }
    }

    public int getIndex(K key) {
        int index = 0;
        if (key != null) {
            index = (key.hashCode() & 0x7fffffff) % table.length;
        }
        return index;
    }
}
