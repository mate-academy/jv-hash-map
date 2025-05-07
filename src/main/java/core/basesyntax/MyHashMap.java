package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEAFULT_SIZE = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;

    private Entry<K, V>[] table;

    public MyHashMap() {
        table = new Entry[DEAFULT_SIZE];
        size = 0;
    }

    private static class Entry<K, V> {
        private K key;
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
        int index = getIndex(key);
        Entry<K, V> current = table[index];

        if (key == null) {
            if (current == null) {
                table[index] = new Entry<K, V>(key, value, null);
                size++;
                return;
            } else {
                while (current != null) {
                    if (current.key == null) {
                        current.value = value;
                        return;
                    }
                    current = current.next;
                }
                table[index] = new Entry<K, V>(key, value, table[index]);
                size++;
                return;
            }
        }

        while (current != null) {
            if (key.equals(current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Entry<K, V> newEntry = new Entry<K, V>(key, value, table[index]);
        table[index] = newEntry;
        size++;

        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Entry<K, V> current = table[index];

        if (key == null) {
            while (current != null) {
                if (current.key == null) {
                    return current.value;
                }
                current = current.next;
            }
            return null;
        }

        while (current != null) {
            if (key.equals(current.key)) {
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

    private void resize() {
        int newCapacity = table.length * 2;
        Entry<K, V>[] newTable = new Entry[newCapacity];

        for (Entry<K, V> entry : table) {
            while (entry != null) {
                int newIndex = getIndex(entry.key, newCapacity);
                Entry<K, V> next = entry.next;
                entry.next = newTable[newIndex];
                newTable[newIndex] = entry;
                entry = next;
            }
        }

        table = newTable;
    }

    private int getIndex(K key) {
        return getIndex(key, table.length);
    }

    private int getIndex(K key, int capacity) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

}
