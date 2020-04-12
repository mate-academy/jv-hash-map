package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private Entry<K, V>[] bucketsTable;
    private int size;
    private int newCapacity;

    public MyHashMap() {
        size = 0;
        newCapacity = 16;
        bucketsTable = new Entry[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        double threshold = (double)size / bucketsTable.length;
        if (threshold >= DEFAULT_LOAD_FACTOR) {
            resizeTable();
        }
        if (key == null) {
            putNullKey(value);
            return;
        }
        int index = key == null
                ? 0 : getHash(key);
        if (bucketsTable[index] == null) {
            bucketsTable[index] = new Entry<>(key, value, null);
            size++;
            return;
        }
        Entry<K, V> newEntry = bucketsTable[index];
        while (newEntry != null) {
            if (key.equals(newEntry.key)) {
                newEntry.value = value;
                return;
            }
            newEntry = newEntry.nextEntry;
        }
        bucketsTable[index] = new Entry<>(key, value, bucketsTable[index]);
        size++;
    }

    private void putNullKey(V value) {
        Entry<K, V> newEntry = bucketsTable[0];
        while (newEntry != null) {
            if (newEntry.key == null) {
                newEntry.value = value;
                return;
            }
            newEntry = newEntry.nextEntry;
        }
        bucketsTable[0] = new Entry<>(null, value, bucketsTable[0]);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = key == null
                ? 0 : getHash(key);
        Entry<K, V> entry = bucketsTable[index];
        while (entry != null) {
            if (entry.key == key
                    || entry.key != null && entry.key.equals(key)) {
                return entry.value;
            }
            entry = entry.nextEntry;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        return Math.abs(key.hashCode()) % newCapacity;
    }

    private void resizeTable() {
        newCapacity = bucketsTable.length * 2;
        Entry<K, V>[] newBucketsTable = new Entry[newCapacity];
        for (Entry<K, V> entry : bucketsTable) {
            while (entry != null) {
                int index = entry.key == null
                        ? 0 : getHash(entry.key);
                if (newBucketsTable[index] == null) {
                    newBucketsTable[index] = new Entry<>(entry.key, entry.value, null);
                    entry = entry.nextEntry;
                    continue;
                }
                newBucketsTable[index] = new Entry<>(entry.key, entry.value,
                        newBucketsTable[index]);
                entry = entry.nextEntry;
            }
        }
        bucketsTable = newBucketsTable;
    }

    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> nextEntry;

        private Entry(K key, V value, Entry<K, V> nextEntry) {
            this.key = key;
            this.value = value;
            this.nextEntry = nextEntry;
        }
    }
}
