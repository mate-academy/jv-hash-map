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
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        int index = key == null ? 0 : Math.abs(key.hashCode()) % table.length;
        Entry<K, V> current = table[index];
        if (current == null) {
            table[index] = new Entry<>(key, value, null);
            size++;
        } else {
            while (current != null) {
                if ((key == null && current.key == null)
                        || (key != null && key.equals(current.key))) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = new Entry<>(key, value, null);
                    size++;
                    return;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = key == null ? 0 : Math.abs(key.hashCode()) % table.length;
        Entry<K, V> current = table[index];
        while (current != null) {
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
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
        Entry<K, V>[] oldTable = table;
        table = new Entry[oldTable.length * 2];
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

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
