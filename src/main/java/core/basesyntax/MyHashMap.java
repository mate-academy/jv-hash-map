package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int CAPACITY = 16;
    private int initialCapacity;
    public Entry<K, V>[] table;
    private int size = 0;

    public MyHashMap() {
        table = new Entry[CAPACITY];
        initialCapacity = CAPACITY;
    }

    private int countBuckPos(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @Override
    public void put(K key, V value) {
        resise();
        Entry<K, V> newEntry = new Entry<>(key, value);
        int position = countBuckPos(newEntry.getKey());
        Entry temp = table[position];
        if (temp == null) {
            table[position] = newEntry;
            size++;
            return;
        }

        while (temp != null) {
            if (newEntry.getKey() == temp.getKey() || newEntry.getKey() != null
                    && newEntry.getKey().equals(temp.getKey())) {
                temp.setValue(value);
                return;
            }
            if (temp.getNext() == null) {
                temp.setNext(newEntry);
                size++;
                return;
            }
            temp = temp.getNext();
        }
    }

    @Override
    public V getValue(K key) {
        Entry<K, V> temp = table[countBuckPos(key)];
        while (temp != null) {
            if (temp.getKey() == key || (key != null && key.equals(temp.getKey()))) {
                return temp.getValue();
            }
            temp = temp.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resise() {
        if (LOAD_FACTOR * getSize() >= initialCapacity) {
            size = 0;
            Entry<K, V>[] tempTable = table;
            initialCapacity = initialCapacity * 2;
            table = new Entry[table.length * 2];
            for (Entry<K, V> entry : tempTable) {
                while (entry != null) {
                    put(entry.getKey(), entry.getValue());
                    entry = entry.getNext();
                }
            }
        }
    }

    private static class Entry<K, V> {
        private int hash;
        private K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = hashCode();
            this.next = null;
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
            return hash == entry.hash
                    && Objects.equals(key, entry.key)
                    && Objects.equals(value, entry.value);
        }

        @Override
        public int hashCode() {
            return Math.abs(Objects.hash(key) ^ Objects.hash(value));
        }

        public int getHash() {
            return hash;
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

        public Entry<K, V> getNext() {
            return next;
        }

        public void setNext(Entry<K, V> next) {
            this.next = next;
        }
    }
}
