package core.basesyntax;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    private Entry<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Entry[DEFAULT_CAPACITY];
    }

    static class Entry<K, V> {
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
            if (Objects.equals(key1, key2)) {
                Object value1 = getValue();
                Object value2 = entry.getValue();
                return Objects.equals(value1, value2);
            }
            return false;
        }

        public final int hashCode() {
            return (key == null ? 0 : Math.abs(key.hashCode()));
        }
    }

    private int indexFor(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % DEFAULT_CAPACITY);
    }

    private void putForNullKey(V value) {
        for (Entry<K, V> entry = table[0]; entry != null; entry = entry.next) {
            if (entry.key == null) {
                entry.value = value;
                return;
            }
        }
        addEntry(null, value, 0);
    }

    private void addEntry(K key, V value, int index) {
        Entry<K, V> entry = table[index];
        table[index] = new Entry<>(key, value, entry);
        if (size++ >= threshold) {
            resize(2 * table.length);
        }
    }

    private void resize(int newCapacity) {
        Entry<K, V>[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }

    private void transfer(Entry<K, V>[] newTable) {
        Entry<K, V>[] src = table;
        for (int j = 0; j < src.length; j++) {
            Entry<K, V> entry = src[j];
            if (entry != null) {
                src[j] = null;
            }
            while (entry != null) {
                Entry<K, V> next = entry.next;
                int i = indexFor(entry.key);
                entry.next = newTable[i];
                newTable[i] = entry;
                entry = next;
            }
        }
    }

    private V getForNullKey() {
        for (Entry<K, V> entry = table[0]; entry != null; entry = entry.next) {
            if (entry.key == null) {
                return entry.value;
            }
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
        } else {
            int index = indexFor(key);
            for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next) {
                if (key == entry.key || key.equals(entry.key)) {
                    entry.value = value;
                    return;
                }
            }
            addEntry(key, value, index);
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        } else {
            for (Entry<K, V> entry = table[indexFor(key)]; entry != null; entry = entry.next) {
                if (key == entry.key || key.equals(entry.key)) {
                    return entry.value;
                }
            }
            return null;
        }
    }

    @Override
    public int getSize() {
        return size;
    }
}
