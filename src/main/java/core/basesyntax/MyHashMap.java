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

    public MyHashMap() {
        size = 0;
        bucketsTable = new Entry[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        double threshold = (double)size / bucketsTable.length;
        if (threshold >= DEFAULT_LOAD_FACTOR) {
            resizeTable();
        }
        int index = getHash(key);
        Entry<K, V> newEntry = bucketsTable[index];
        if (bucketsTable[index] == null) {
            bucketsTable[index] = new Entry<>(key, value, null);
            size++;
            return;
        }
        while (newEntry != null) {
            if (key == null) {
                if (newEntry.key == null) {
                    newEntry.value = value;
                    return;
                }
            } else if (key.equals(newEntry.key)) {
                newEntry.value = value;
                return;
            }
            newEntry = newEntry.nextEntry;
        }
        bucketsTable[index] = new Entry<>(key, value, bucketsTable[index]);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getHash(key);
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
        return key == null
                ? 0 : Math.abs(key.hashCode()) % bucketsTable.length;
    }

    private void resizeTable() {
        Entry<K,V>[] tempArray = bucketsTable;
        bucketsTable = new Entry[bucketsTable.length * 2];
        for (Entry<K, V> entry : tempArray) {
            while (entry != null) {
                int index = getHash(entry.key);
                bucketsTable[index] = new Entry<>(entry.key, entry.value,
                        bucketsTable[index]);
                put(entry.key, entry.value);
                entry = entry.nextEntry;
            }
        }
    }

    private static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> nextEntry;

        private Entry(K key, V value, Entry<K, V> nextEntry) {
            this.key = key;
            this.value = value;
            this.nextEntry = nextEntry;
        }
    }
}
