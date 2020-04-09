package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Entry<K, V>[] table;
    private double threshold;
    private int size = 0;

    public MyHashMap() {
        table = new Entry[CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        threshold = (double) size / table.length;
        if (threshold >= LOAD_FACTOR) {
            resize();
        }
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int hash = hash(key, table.length);
        if (table[hash] == null) {
            table[hash] = new Entry<K, V>(key, value, null);
            size++;
            return;
        }
        Entry<K, V> entry = table[hash];
        while (entry != null) {
            if (key.equals(entry.key)) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }
        addEntry(hash, key, value);
    }

    private void addEntry(int hash, K key, V value) {
        table[hash] = new Entry<K, V>(key, value, table[hash]);
        size++;
    }

    private void putForNullKey(V value) {
        Entry<K, V> entry = table[0];
        while (entry != null) {
            if (entry.key == null) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }
        addEntry(0, null, value);
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key, table.length);
        Entry<K, V> entry = table[hash];
        while (entry != null) {
            if (entry.key == null && key == null
                    || entry.key != null && entry.key.equals(key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Entry<K, V>[] newTable = new Entry[table.length * 2];
        for (Entry<K, V> entry : table) {
            while (entry != null) {
                int hash = hash(entry.key, newTable.length);
                ;
                if (newTable[hash] == null) {
                    newTable[hash] = new Entry<K, V>(entry.key, entry.value, null);
                    entry = entry.next;
                    continue;
                }
                newTable[hash] = new Entry<K, V>(entry.key, entry.value, newTable[hash]);
                entry = entry.next;
            }
        }
        table = newTable;
    }

    private int hash(K kay, int lengthTable) {
        int hashCode = 0;
        if (kay != null) {
            hashCode = kay.hashCode();
        }
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        return hashCode % lengthTable;
    }

    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
