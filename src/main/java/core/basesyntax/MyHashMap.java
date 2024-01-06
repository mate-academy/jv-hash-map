package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;

    private Entry<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Entry[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }

        int hash = hash(key);
        int index = indexFor(hash, table.length);

        for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if ((entry.hash == hash) && (key == entry.key || key.equals(entry.key))) {
                entry.value = value; // Update the value if key already exists
                return;
            }
        }
        addEntry(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }

        int hash = hash(key);
        int index = indexFor(hash, table.length);

        for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if ((entry.hash == hash) && (key == entry.key || key.equals(entry.key))) {
                return entry.value; // Return the value if key is found
            }
        }

        return null; // Return null if key is not found
    }

    @Override
    public int getSize() {
        return size;
    }

    // method to handle null key
    private void putForNullKey(V value) {
        for (Entry<K, V> entry = table[0]; entry != null; entry = entry.next) {
            if (entry.key == null) {
                entry.value = value; // Update the value if null key already exists
                return;
            }
        }
        addEntry(0, null, value, 0);
    }

    // method to handle null key
    private V getForNullKey() {
        for (Entry<K, V> entry = table[0]; entry != null; entry = entry.next) {
            if (entry.key == null) {
                return entry.value; // Return the value if null key is found
            }
        }
        return null; // Return null if null key is not found
    }

    // method to calculate hash
    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    // method to calculate index for the given hash and table length
    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    // method to add a new entry to the table
    private void addEntry(int hash, K key, V value, int index) {
        Entry<K, V> entry = table[index];
        table[index] = new Entry<>(hash, key, value, entry);
        size++;

        // Check if resizing is needed
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = table.length * RESIZE_FACTOR;
        Entry<K, V>[] newTable = new Entry[newCapacity];

        for (Entry<K, V> entry : table) {
            while (entry != null) {
                Entry<K, V> next = entry.next;
                int index = indexFor(entry.hash, newCapacity);
                entry.next = newTable[index];
                newTable[index] = entry;
                entry = next;
            }
        }
        table = newTable;
    }

    static class Entry<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Entry<K, V> next;

        Entry(int hash, K key, V value, Entry<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
