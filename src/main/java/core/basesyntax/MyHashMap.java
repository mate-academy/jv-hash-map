package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float GROW_FACTOR = 0.75f;

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
        for (Entry<K, V> entry = table[calculateIndex(hash(key))];
                entry != null; entry = entry.next) {
            if (key.equals(entry.key)) {
                entry.value = value;
                return;
            }
        }
        addEntry(hash(key), key, value, calculateIndex(hash(key)));
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }
        for (Entry<K, V> entry = table[calculateIndex(hash(key))];
                entry != null; entry = entry.next) {
            if ((key.equals(entry.key))) {
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
        table[bucketIndex] = new Entry<>(key, value, entry);
        size++;

        if (size > (GROW_FACTOR * table.length)) {
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
                    int i = indexFor(hash(entry.key), newCapacity);
                    entry.next = newTable[i];
                    newTable[i] = entry;
                    entry = next;
                } while (entry != null);
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    public int calculateIndex(int hash) {
        return indexFor(hash, table.length);
    }

    private int indexFor(int value, int length) {
        return value & (length - 1);
    }

    private static class Entry<K, V> {
        private Entry<K, V> next;
        private final K key;
        private V value;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
