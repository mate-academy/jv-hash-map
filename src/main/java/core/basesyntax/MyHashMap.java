package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Entry<K, V>[] table;

    public MyHashMap() {
        table = new Entry[INITIAL_CAPACITY];
        threshold = countThreshold(INITIAL_CAPACITY);
    }

    private static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        Entry<K, V> newEntry = new Entry<>(key, value);
        if (key == null) {
            putForNullKey(newEntry);
            return;
        }
        int index = hash(key.hashCode());
        if (table[index] == null) {
            table[index] = newEntry;
            checkSize(++size);
            return;
        }
        Entry<K, V> entryByIndex = table[index];
        while (entryByIndex != null) {
            if (entryByIndex.key.equals(key)) {
                entryByIndex.value = value;
                return;
            }
            if (entryByIndex.next == null) {
                entryByIndex.next = newEntry;
                checkSize(++size);
                return;
            }
            entryByIndex = entryByIndex.next;
        }
    }

    private void putForNullKey(Entry<K, V> nullKeyEntry) {
        if (table[0] == null) {
            table[0] = nullKeyEntry;
            size++;
        } else {
            table[0].value = nullKeyEntry.value;
        }
    }

    private void checkSize(int size) {
        if (size > threshold) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Entry<K, V>[] oldTable = table;
        table = new Entry[newCapacity];
        threshold = countThreshold(newCapacity);
        rehash(oldTable);
    }

    private void rehash(Entry<K, V>[] oldTable) {
        size = 0;
        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    private int hash(int hashcode) {
        return Math.abs(hashcode) % (table.length - 1) + 1;
    }

    private static int countThreshold(int capacity) {
        return Math.round(capacity * LOAD_FACTOR);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return table[0].value;
        }
        int index = hash(key.hashCode());
        Entry<K, V> entry = table[index];
        while (entry != null) {
            if (entry.key.equals(key)) {
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
}