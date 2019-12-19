package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int CAPACITY = 10;
    private int initialCapacity;
    public Entry<K, V>[] table;
    private int size = 0;

    public MyHashMap() {
        initialCapacity = CAPACITY;
        table = new Entry[initialCapacity];
    }

    private int countBuckPos(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void putInEmptyBucket(int position, Entry<K, V> entry) {
        table[position] = entry;
        size++;
    }

    @Override
    public void put(K key, V value) {
        resise();
        Entry<K, V> newEntry = new Entry<>(key, value);
        int position = countBuckPos(newEntry.getKey());
        Entry temp = table[position];
        if (temp == null) {
            putInEmptyBucket(position, newEntry);
            return;
        }
        while (position < table.length) {
            if (position == table.length - 1) {
                position = 0;
            }

            temp = table[position];
            if (temp == null) {
                putInEmptyBucket(position, new Entry<>(key, value));
                return;
            }
            if ((key == null && temp.getKey() == null)
                    || (key != null && key.equals(temp.getKey()))) {
                temp.setValue(value);
                return;
            }
            position++;
        }
    }

    @Override
    public V getValue(K key) {
        int position = countBuckPos(key);
        Entry<K, V> temp = table[position];
        if (temp != null) {
            while (position < table.length) {
                if (position == table.length - 1) {
                    position = 0;
                }
                if (temp.getKey() == key || (key != null && key.equals(temp.getKey()))) {
                    return temp.getValue();
                }
                temp = table[position];
                position++;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resise() {
        if (size == initialCapacity - 1) {
            size = 0;
            Entry<K, V>[] tempTable = table;
            initialCapacity = initialCapacity * 2;
            table = new Entry[table.length * 2];
            for (int i = 0; i < tempTable.length; i++) {
                if (tempTable[i] != null) {
                    put(tempTable[i].getKey(), tempTable[i].getValue());
                }
            }
        }
    }

    private static class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
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
            return Objects.equals(key, entry.key)
                    && Objects.equals(value, entry.value);
        }

        @Override
        public int hashCode() {
            return Math.abs(Objects.hash(key) ^ Objects.hash(value));
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
