package core.basesyntax;


public class MyHashMap<K, V> implements MyMap<K, V> {

    private static class Entry<K, V> {
        private K key;
        private V value;
        Entry<K, V> next;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }

    private Entry<K, V>[] table;
    private int size;
    private float load_factor = 0.75f;
    private static int default_capacity = 1 << 16;
    private int threshold = (int) (default_capacity * load_factor);

    public MyHashMap() {
        this.table = new Entry[default_capacity];
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        if (table[index] == null) {
            table[index] = new Entry<>(key, value);
            size++;
            return;
        }
        if (index == 0) {
            table[index].value = value;
            return;
        }
        Entry<K, V> entry = table[index];
        while (entry != null) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            } else
                entry = entry.next;
        }
        if (size++ > threshold) {
            rehash();
            index = hash(key);
        }
        addEntry(key, value, index);
    }

    private void addEntry(K key, V value, int idx) {
        Entry<K, V> entry = new Entry<>(key, value);
        entry.next = table[idx];
        table[idx] = entry;
    }

    private void rehash() {
        Entry<K, V>[] oldTable = table;
        int newCapacity = table.length * 2 + 1;
        threshold = (int) (newCapacity * load_factor);
        table = new Entry[newCapacity];
        for (int i = table.length - 1; i >= 0; i--) {
            Entry<K, V> entry = oldTable[i];
            while (entry != null) {
                int index = hash(entry.key);
                Entry<K, V> next = entry.next;
                entry.next = table[index];
                table[index] = entry;
                entry = next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        if (table[index] == null) {
            return null;
        }
        if (index > table.length - 1) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (index == 0) {
            return table[index].value;
        }
        Entry<K, V> entry = table[index];
        while (entry != null) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    public int getSize() {
        return size;
    }
}
