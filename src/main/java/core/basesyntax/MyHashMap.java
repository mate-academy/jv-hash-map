package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;

    private Entry<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Entry[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(key);
        Entry<K, V> newEntry = new Entry<>(key, value);

        if (table[index] == null) {
            table[index] = newEntry;
        } else {
            Entry<K, V> current = table[index];
            while (current != null) {

                if (currentObjectNull(current, key) || currentObjectTrue(current, key)) {
                    current.value = value;
                    return;
                }

                if (current.next == null) {
                    current.next = newEntry;
                    break;
                }
                current = current.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Entry<K, V> current = table[index];

        while (current != null) {
            if (currentObjectTrue(current, key) || currentObjectNull(current, key)) {
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
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private boolean currentObjectTrue(Entry<K, V> current, K object) {
        return (current.key != null && current.key.equals(object));
    }

    private boolean currentObjectNull(Entry<K, V> current, K object) {
        return (current.key == null && object == null);
    }

    private void resize() {
        Entry<K, V>[] oldTable = table;
        table = new Entry[oldTable.length * GROW_FACTOR];
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

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
