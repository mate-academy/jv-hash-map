package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int INITIAL_CAPACITY = 1 << 4;
    static final int CAPACITY_MULTIPLIER = 2;
    static final float LOAD_FACTOR = 0.75f;

    private Entry<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Entry[INITIAL_CAPACITY];
        threshold = (int) (LOAD_FACTOR * INITIAL_CAPACITY);
    }

    static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        resizeCheck();
        if (table[getTableIndex(key)] == null) {
            table[getTableIndex(key)] = new Entry<>(key, value, null);
            size++;
        } else {
            putWithCollision(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        Entry<K, V> entry = table[getTableIndex(key)];
        while (entry != null) {
            if (key == entry.key || (key != null && key.equals(entry.key))) {
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

    private int getTableIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resizeCheck() {
        if (size == threshold) {
            int newCapacity = table.length * CAPACITY_MULTIPLIER;
            threshold = (int) (LOAD_FACTOR * table.length);
            Entry<K, V>[] oldTable = table;
            table = new Entry[newCapacity];
            size = 0;
            for (Entry<K, V> entry : oldTable) {
                while (entry != null) {
                    put(entry.key, entry.value);
                    entry = entry.next;
                }
            }
        }
    }

    private void putWithCollision(K key, V value) {
        Entry<K, V> entry = table[getTableIndex(key)];
        while (entry != null) {
            if (key == entry.key || key != null && key.equals(entry.key)) {
                entry.value = value;
                return;
            }
            if (entry.next == null) {
                entry.next = new Entry<K, V>(key, value, null);
                size++;
                return;
            }
            entry = entry.next;
        }
    }
}
