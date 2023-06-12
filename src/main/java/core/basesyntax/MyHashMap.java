package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;

    private Entry<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Entry[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Entry<K, V> newEntry = new Entry<>(key, value);

        if (table[index] == null) {
            table[index] = newEntry;
            size++;
        } else {
            Entry<K, V> currentEntry = table[index];
            while (currentEntry != null) {
                if (keyEquals(currentEntry.key, key)) {
                    currentEntry.value = value;
                    return;
                }
                currentEntry = currentEntry.next;
            }
            newEntry.next = table[index];
            table[index] = newEntry;
            size++;
        }

        if (needsResize()) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Entry<K, V> entry = table[index];

        while (entry != null) {
            if (keyEquals(entry.key, key)) {
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
        return Math.abs(key.hashCode()) % table.length;
    }

    private boolean keyEquals(K key1, K key2) {
        if (key1 == null && key2 == null) {
            return true;
        }
        if (key1 == null || key2 == null) {
            return false;
        }
        return key1.equals(key2);
    }

    private boolean needsResize() {
        float loadFactor = (float) size / table.length;
        return loadFactor >= DEFAULT_LOAD_FACTOR;
    }

    private void resize() {
        Entry<K, V>[] oldTable = table;
        table = new Entry[table.length * RESIZE_FACTOR];
        size = 0;

        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
