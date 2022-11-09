package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private float threshold;
    private int capacity;
    private int size;
    private Entry<K, V>[] table;

    public MyHashMap() {
        table = new Entry[DEFAULT_INITIAL_CAPACITY];
        capacity = table.length;
        threshold = DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int indexOfBucket = getBucketIndex(key, table);
        Entry<K, V> element = table[indexOfBucket];
        Entry<K, V> newElement = new Entry<>(key, value);
        if (element == null) {
            table[indexOfBucket] = newElement;
            size++;
        } else {
            while (element.next != null) {
                if (Objects.equals(element.key, key)) {
                    element.value = value;
                    return;
                }
                element = element.next;
            }
            if (Objects.equals(element.key, key)) {
                element.value = value;
                return;
            }
            element.next = newElement;
            size++;
            if (size >= threshold) {
                capacity = table.length * 2;
                threshold = capacity * LOAD_FACTOR;
                resize(capacity);
            }
        }
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key, table);
        Entry<K, V> element = table[bucketIndex];
        if (element == null) {
            return null;
        }
        while (element != null) {
            if (Objects.equals(element.key, key)) {
                return element.value;
            }
            element = element.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucketIndex(Object key, Entry<K, V>[] table) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }

    private void resize(int newCapacity) {
        Entry<K, V>[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
    }

    void transfer(Entry<K, V>[] newTable) {
        for (Entry<K, V> e : table) {
            while (e != null) {
                Entry<K, V> next = e.next;
                int index = getBucketIndex(e.key, newTable);
                e.next = newTable[index];
                newTable[index] = e;
                e = next;
            }
        }
    }

    public static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
