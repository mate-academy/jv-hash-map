package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Entry<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Entry<K, V>[]) new Entry[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Entry<K, V> entry = table[index];
        while (entry != null) {
            if ((key == null && entry.key == null) || (key != null && key.equals(entry.key))) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }
        addEntry(index, key, value);
        size++;
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Entry<K, V> entry = table[index];
        while (entry != null) {
            if ((key == null && entry.key == null) || (key != null && key.equals(entry.key))) {
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

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
    }

    private void addEntry(int index, K key, V value) {
        Entry<K, V> entry = new Entry<>(key, value);
        if (table[index] == null) {
            table[index] = entry;
        } else {
            Entry<K, V> current = table[index];
            while (current.next != null) {
                current = current.next;
            }
            current.next = entry;
        }
    }

    private void resize() {
        Entry<K, V>[] oldTable = table;
        table = (Entry<K, V>[]) new Entry[table.length * 2];
        size = 0;

        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}


