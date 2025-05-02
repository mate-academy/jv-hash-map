package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Entry<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Entry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }

        int hash = hash(key);
        int index = indexFor(hash, table.length);

        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if ((e.key == key) || (e.key != null && e.key.equals(key))) {
                e.value = value;
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

        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if ((e.key == key) || (e.key != null && e.key.equals(key))) {
                return e.value;
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putForNullKey(V value) {
        for (Entry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                e.value = value;
                return;
            }
        }
        addEntry(0, null, value, 0);
    }

    private V getForNullKey() {
        for (Entry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                return e.value;
            }
        }
        return null;
    }

    private void addEntry(int hash, K key, V value, int bucketIndex) {
        Entry<K, V> e = table[bucketIndex];
        table[bucketIndex] = new Entry<>(hash, key, value, e);
        size++;

        if (size >= table.length * LOAD_FACTOR) {
            resize(2 * table.length);
        }
    }

    private void resize(int newCapacity) {
        Entry<K, V>[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
    }

    private void transfer(Entry<K, V>[] newTable) {
        Entry<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;

        for (int i = 0; i < oldCapacity; i++) {
            Entry<K, V> e = oldTable[i];
            while (e != null) {
                Entry<K, V> next = e.next;
                int index = indexFor(e.hash, newTable.length);
                e.next = newTable[index];
                newTable[index] = e;
                e = next;
            }
        }
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
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
    }
}
