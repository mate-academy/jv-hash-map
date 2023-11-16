package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_RESIZE_FACTOR = 2;

    private Entry<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Entry[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(hash(key), table.length);
        Entry<K, V> entry = table[index];
        while (entry != null) {
            if ((key == null && entry.getKey() == null)
                    || (key != null && key.equals(entry.getKey()))) {
                entry.setValue(value);
                return;
            }
            entry = entry.getNext();
        }

        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.setNext(table[index]);
        table[index] = newEntry;
        size++;
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = getIndex(hash, table.length);
        Entry<K, V> entry = table[index];
        while (entry != null) {
            if ((key == null && entry.getKey() == null)
                    || (key != null && key.equals(entry.getKey()))) {
                return entry.getValue();
            }
            entry = entry.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = table.length * INITIAL_RESIZE_FACTOR;
        Entry<K, V>[] newTable = new Entry[newCapacity];
        for (Entry<K, V> entry : table) {
            while (entry != null) {
                putInNewTable(entry, newTable, newCapacity);
                entry = entry.getNext();
            }
        }

        table = newTable;
    }

    private void putInNewTable(Entry<K, V> entry, Entry<K, V>[] newTable, int newCapacity) {
        int index = getIndex(hash(entry.getKey()), newCapacity);

        Entry<K, V> existing = newTable[index];
        Entry<K, V> newEntry = new Entry<>(entry.getKey(), entry.getValue());

        newEntry.setNext(existing);
        newTable[index] = newEntry;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(int hash, int tableLength) {
        return hash & (tableLength - 1);
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
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
