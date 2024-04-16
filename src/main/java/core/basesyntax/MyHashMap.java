package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Entry<K, V>[] table;
    private int size;
    private int capacity;

    public MyHashMap() {
        table = new Entry[DEFAULT_INITIAL_CAPACITY];
        capacity = DEFAULT_INITIAL_CAPACITY;
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;
        private final int hash;

        Entry(int hash, K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= capacity * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && (key == e.key
                    || (key != null && key.equals(e.key)))) {
                e.value = value;
                return;
            }
        }
        addEntry(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && (key == e.key
                    || (key != null && key.equals(e.key)))) {
                return e.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addEntry(int hash, K key, V value, int index) {
        Entry<K, V> newEntry = new Entry<>(hash, key, value, table[index]);
        table[index] = newEntry;
        size++;
    }

    private int hash(K key) {
        int hashCode;
        return (key == null) ? 0 : (hashCode = key.hashCode()) ^ (hashCode >>> 16);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Entry<K, V>[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        capacity = newCapacity;
    }

    private void transfer(Entry<K, V>[] newTable) {
        for (int i = 0; i < table.length; i++) {
            Entry<K, V> oldEntry = table[i];
            if (oldEntry != null) {
                table[i] = null;
                do {
                    Entry<K, V> next = oldEntry.next;
                    int index = indexFor(oldEntry.hash, newTable.length);
                    oldEntry.next = newTable[index];
                    newTable[index] = oldEntry;
                    oldEntry = next;
                } while (oldEntry != null);
            }
        }
    }
}
