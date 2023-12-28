package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Entry<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Entry[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Entry<>(key, value);
            size++;
        } else {
            Entry<K, V> current = table[index];
            while (current != null) {
                if (isEqual(current.key, key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            Entry<K, V> newEntry = new Entry<>(key, value);
            newEntry.next = table[index];
            table[index] = newEntry;
            size++;
        }
        if (size > table.length * LOAD_FACTOR) {
            rehash();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Entry<K, V> current = table[index];
        while (current != null) {
            if (isEqual(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(K key) {
        return hash(key) & (table.length - 1);
    }

    private void rehash() {
        Entry<K, V>[] oldTable = table;
        table = new Entry[2 * oldTable.length];
        size = 0;
        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    private boolean isEqual(K key1, K key2) {
        if (key1 == null) {
            return key2 == null;
        }
        return key1.equals(key2);
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
    }
}
