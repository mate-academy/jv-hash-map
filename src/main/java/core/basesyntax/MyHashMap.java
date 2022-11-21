package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Entry<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Entry[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key, table);
        Entry<K, V> element = table[bucketIndex];
        Entry<K, V> newElement = new Entry<>(key, value);
        if (element == null) {
            table[bucketIndex] = newElement;
        } else {
            while (element.next != null) {
                if (keyChecking(element, key, value)) {
                    return;
                }
                element = element.next;
            }
            if (keyChecking(element, key, value)) {
                return;
            }
            element.next = newElement;
        }
        size++;
        if (size == threshold) {
            Entry<K, V>[] newTable = new Entry[table.length * 2];
            copy(newTable);
            table = newTable;
            threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
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

    private int getBucketIndex(K key, Entry<K, V>[] table) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void copy(Entry<K, V>[] newTable) {
        for (Entry<K, V> element : table) {
            while (element != null) {
                Entry<K, V> next = element.next;
                int index = getBucketIndex(element.key, newTable);
                element.next = newTable[index];
                newTable[index] = element;
                element = next;
            }
        }
    }

    private boolean keyChecking(Entry<K, V> element, K key, V value) {
        if (Objects.equals(element.key, key)) {
            element.value = value;
            return true;
        }
        return false;
    }

    private class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
