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
        if (key == null) {
            if (table[0] == null) {
                table[0] = new Entry<>(null, value);
            } else {
                Entry<K, V> current = table[0];
                while (current != null) {
                    if (current.key == null) {
                        current.value = value;
                        return;
                    }
                    current = current.next;
                }
                Entry<K, V> entry = new Entry<>(null, value);
                entry.next = table[0];
                table[0] = entry;
            }
        } else {
            int index = hash(key);
            Entry<K, V> entry = new Entry<>(key, value);

            if (table[index] == null) {
                table[index] = entry;
            } else {
                Entry<K, V> current = table[index];
                while (current != null) {
                    if (current.key != null && current.key.equals(key)) {
                        current.value = value;
                        return;
                    }
                    current = current.next;
                }
                entry.next = table[index];
                table[index] = entry;
            }
        }
        size++;
        if ((double) size / table.length >= LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Entry<K, V> current = table[index];

        while (current != null) {
            if (current.key == null && key == null
                    || current.key != null && current.key.equals(key)) {
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
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }

    private static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private void resize() {
        Entry<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        table = new Entry[newCapacity];
        size = 0;

        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                int index = hash(entry.key);
                Entry<K, V> newEntry = new Entry<>(entry.key, entry.value);
                newEntry.next = table[index];
                table[index] = newEntry;
                entry = entry.next;
                size++;
            }
        }
    }
}
