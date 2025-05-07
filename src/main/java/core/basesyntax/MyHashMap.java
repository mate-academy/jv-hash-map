package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Entry<K, V>[] table = new Entry[DEFAULT_CAPACITY];
    private int size = 0;

    private static class Entry<K, V> {

        private final K key;
        private V value;
        private Entry<K, V> next;

        private Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= DEFAULT_LOAD_FACTOR * table.length) {
            resizing();
        }
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int hashCode = key.hashCode();
        int index = (table.length - 1) & hashCode;
        Entry<K, V> entry = table[index];
        if (entry == null) {
            table[index] = new Entry<>(key, value, null);
            size++;
            return;
        }
        Entry<K, V> currentEntry = entry;
        while (true) {
            if (key.equals(currentEntry.key)) {
                currentEntry.value = value;
                return;
            }
            if (currentEntry.next == null) {
                currentEntry.next = new Entry<>(key, value, null);
                size++;
                return;
            }
            currentEntry = currentEntry.next;
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        if (key == null) {
            return getForNullKey();
        }
        int hashCode = key.hashCode();
        int index = (table.length - 1) & hashCode;
        Entry<K, V> entry = table[index];
        while (entry != null) {
            if (key.equals(entry.key)) {
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

    private void putForNullKey(V value) {
        Entry<K, V> entry = table[0];
        if (entry == null) {
            table[0] = new Entry<>(null, value, null);
            size++;
        } else {
            Entry<K, V> current = entry;
            while (true) {
                if (current.key == null) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = new Entry<>(null, value, null);
                    size++;
                    return;
                }
                current = current.next;
            }
        }
    }

    private V getForNullKey() {
        Entry<K, V> entry = table[0];
        while (entry != null) {
            if (entry.key == null) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    private void resizing() {
        size = 0;
        Entry<K, V>[] newTable = new Entry[table.length * 2];
        Entry<K, V>[] oldTable = table;
        table = newTable;
        for (int i = 0; i < oldTable.length; i++) {
            Entry<K, V> currentEntry = oldTable[i];
            while (currentEntry != null) {
                put(currentEntry.key, currentEntry.value);
                currentEntry = currentEntry.next;
            }
        }
    }
}
