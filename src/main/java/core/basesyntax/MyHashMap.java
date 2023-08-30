package core.basesyntax;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private Entry<K, V>[] table;
    private int size;

    public MyHashMap() {
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        table = new Entry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize(2 * table.length);
        }
        int index = getIndex(key);
        Entry<K, V> entry = table[index];
        if (entry == null) {
            table[index] = new Entry<>(key, value, null);
        }
        for (; entry != null; entry = entry.next) {
            if (entry.key == key || entry.key != null && entry.key.equals(key)) {
                entry.value = value;
                return;
            }
            if (entry.next == null) {
                entry.next = new Entry<>(key, value, null);
                break;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        for (Entry<K, V> entry = table[getIndex(key)]; entry != null; entry = entry.next) {
            if (entry.key == key || entry.key != null && entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % DEFAULT_CAPACITY);
    }

    private void resize(int newCapacity) {
        Entry<K, V>[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }

    private void transfer(Entry<K, V>[] newTable) {
        Entry<K, V>[] oldTable = table;
        for (Entry<K, V> kvEntry : oldTable) {
            Entry<K, V> entry = kvEntry;
            while (entry != null) {
                Entry<K, V> next = entry.next;
                int i = getIndex(entry.key);
                entry.next = newTable[i];
                newTable[i] = entry;
                entry = next;
            }
        }
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
            Object key1 = getKey();
            Object key2 = entry.getKey();
            if (Objects.equals(key1, key2) && key1 != null) {
                Object value1 = getValue();
                Object value2 = entry.getValue();
                return Objects.equals(value1, value2);
            }
            return false;
        }
    }
}
