package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

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
        int hash = hash(key.hashCode());
        int index = indexFor(hash, table.length);

        for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if ((entry.hash == hash) && (key.equals(entry.key))) {
                entry.value = value;
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
        int hash = hash(key.hashCode());
        int index = indexFor(hash, table.length);

        for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if ((entry.hash == hash) && (key.equals(entry.key))) {
                return entry.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putForNullKey(V value) {
        for (Entry<K, V> entry = table[0]; entry != null; entry = entry.next) {
            if (entry.key == null) {
                entry.value = value;
                return;
            }
        }
        addEntry(0, null, value, 0);
    }

    private V getForNullKey() {
        for (Entry<K, V> entry = table[0]; entry != null; entry = entry.next) {
            if (entry.key == null) {
                return entry.value;
            }
        }
        return null;
    }

    private void addEntry(int hash, K key, V value, int bucketIndex) {
        Entry<K, V> entry = table[bucketIndex];
        table[bucketIndex] = new Entry<>(hash, key, value, entry);
        size++;

        if (size > (DEFAULT_LOAD_FACTOR * table.length)) {
            resize(2 * table.length);
        }
    }

    private void resize(int newCapacity) {
        Entry<K, V>[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
    }

    private void transfer(Entry<K, V>[] newTable) {
        Entry<K, V>[] src = table;
        int newCapacity = newTable.length;

        for (int j = 0; j < src.length; j++) {
            Entry<K, V> entry = src[j];
            if (entry != null) {
                src[j] = null;
                do {
                    Entry<K, V> next = entry.next;
                    int i = indexFor(entry.hash, newCapacity);
                    entry.next = newTable[i];
                    newTable[i] = entry;
                    entry = next;
                } while (entry != null);
            }
        }
    }

    static final int hash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    static int indexFor(int h, int length) {
        return h & (length - 1);
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

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
