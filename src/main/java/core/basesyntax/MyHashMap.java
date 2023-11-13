package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private static final int ZERO_HASH = 0;

    private Entry<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Entry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putNullKey(value);
            return;
        }

        int hash = key.hashCode();
        int index = getIndex(hash);

        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.key != null && current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = table[index];
        table[index] = newEntry;
        size++;

        if ((double) size / table.length > LOAD_FACTOR) {
            resizeTable();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getNullVal();
        }

        int hash = key.hashCode();
        int index = getIndex(hash);

        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.key != null && current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
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
        private Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private int getIndex(int hash) {
        return (hash == ZERO_HASH) ? ZERO_HASH : Math.abs(hash % table.length);
    }

    private int getIndex(int hash, int tableSize) {
        return Math.abs(hash % tableSize);
    }

    private void resizeTable() {
        int newCapacity = table.length * GROW_FACTOR;
        Entry<K, V>[] newTable = new Entry[newCapacity];

        for (Entry<K, V> current : table) {
            while (current != null) {
                Entry<K, V> next = current.next;
                int newIndex = getIndex(current.key.hashCode(), newCapacity);

                current.next = newTable[newIndex];
                newTable[newIndex] = current;

                current = next;
            }
        }

        table = newTable;
    }

    private void putNullKey(V value) {
        int index = getIndex(ZERO_HASH);

        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.key == null) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Entry<K, V> newEntry = new Entry<>(null, value);
        newEntry.next = table[index];
        table[index] = newEntry;
        size++;

        if ((double) size / table.length > LOAD_FACTOR) {
            resizeTable();
        }
    }

    private V getNullVal() {
        if (table[ZERO_HASH] != null) {
            Entry<K, V> current = table[ZERO_HASH];
            while (current != null) {
                if (current.key == null) {
                    return current.value;
                }
                current = current.next;
            }
        }
        return null;
    }
}
