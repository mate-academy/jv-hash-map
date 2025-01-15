package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int size = 0;
    private Pair<K, V>[] table;
    private int capacity = DEFAULT_CAPACITY;

    public MyHashMap() {
        table = new Pair[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            Pair<K, V> current = table[0];
            while (current != null) {
                if (current.key == null) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = new Pair<>(key, value);
                    size++;
                    return;
                }
                current = current.next;
            }
            table[0] = new Pair<>(key, value);
            size++;
            return;
        }

        if (size >= capacity * DEFAULT_LOAD_FACTOR) {
            resize();
        }

        int index = (key.hashCode() & 0x7FFFFFFF) % capacity;

        if (table[index] == null) {
            table[index] = new Pair<>(key, value);
            size++;
            return;
        }

        Pair<K, V> current = table[index];
        while (current != null) {
            if (current.key == key || (current.key != null && current.key.equals(key))) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Pair<>(key, value);
                size++;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            Pair<K, V> current = table[0];
            while (current != null) {
                if (current.key == null) {
                    return current.value;
                }
                current = current.next;
            }
            return null;
        }

        int index = (key.hashCode() & 0x7FFFFFFF) % capacity;

        Pair<K, V> current = table[index];
        while (current != null) {
            if (current.key == key || (current.key != null && current.key.equals(key))) {
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
        capacity *= 2;
        Pair<K, V>[] newTable = new Pair[capacity];

        for (Pair<K, V> entry : table) {
            while (entry != null) {
                int index = (entry.key == null) ? 0 :
                        (entry.key.hashCode() & 0x7FFFFFFF) % capacity;
                Pair<K, V> newEntry = new Pair<>(entry.key, entry.value);
                if (newTable[index] == null) {
                    newTable[index] = newEntry;
                } else {
                    Pair<K, V> current = newTable[index];
                    while (current.next != null) {
                        current = current.next;
                    }
                    current.next = newEntry;
                }
                entry = entry.next;
            }
        }

        table = newTable;
    }

    private static class Pair<K, V> {
        private K key;
        private V value;
        private Pair<K, V> next;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}


