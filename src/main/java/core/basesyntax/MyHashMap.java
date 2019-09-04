package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Entry<K, V>[] table;
    private int size;

    private static class Entry<K, V> {
        private K key;
        private V value;
        Entry<K, V> next;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public MyHashMap() {
        this.table = new Entry[INITIAL_CAPACITY];
    }

    private int hashCode(K key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(key.hashCode()) % table.length;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = hashCode(key);
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
            } else {
                entry = entry.next;
            }
        }
        if (size++ > table.length * LOAD_FACTOR) {
            rehash();
            index = hashCode(key);
        }
        addEntry(key, value, index);
    }

    private void addEntry(K key, V value, int index) {
        Entry<K, V> entry = new Entry<>(key, value);
        entry.next = table[index];
        table[index] = entry;
    }

    private void rehash() {
        Entry<K, V>[] oldTable = table;
        table = new Entry[table.length * 2 + 1];
        for (int i = oldTable.length - 1; i >= 0; i--) {
            Entry<K, V> entry = oldTable[i];
            while (entry != null) {
                int index = hashCode(entry.key);
                Entry<K, V> next = entry.next;
                entry.next = table[index];
                table[index] = entry;
                entry = next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return table[0].value;
        }
        int index = hashCode(key);
        Entry<K, V> entry = table[index];
        while (entry != null) {
            if (entry.key.equals(key)) {
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
}
