package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Entry<K, V>[] table;
    private int size = 0;
    private int capacity = DEFAULT_CAPACITY;

    public MyHashMap() {
        table = new Entry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= capacity * LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        Entry<K, V> current = table[index];

        if (current == null) {
            table[index] = new Entry<>(key, value);
            size++;
            return;
        }
        while (true) {
            if (current.key == key
                    || (current.key != null && current.key.equals(key))) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Entry<>(key, value);
                size++;
                return;
            }
            current = current.next;
        }
    }

    @Override
     public V getValue(K key) {
        int index = getIndex(key);
        Entry<K, V> current = table[index];

        while (current != null) {
            if (current.key == key
                    || (current.key != null && current.key.equals(key))) {
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

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Entry<K, V>[] newTable = new Entry[newCapacity];

        for (int i = 0; i < table.length; i++) {
            Entry<K, V> current = table[i];

            while (current != null) {
                Entry<K, V> next = current.next;
                int newIndex;
                if (current.key == null) {
                    newIndex = 0;
                } else {
                    newIndex = Math.abs(current.key.hashCode() % newCapacity);

                }
                current.next = newTable[newIndex];
                newTable[newIndex] = current;

                current = next;

            }
        }
        table = newTable;
        capacity = newCapacity;
    }

    private static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
