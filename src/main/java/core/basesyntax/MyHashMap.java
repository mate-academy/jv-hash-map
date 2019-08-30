package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int size;
    private Entry<K, V>[] table;

    public MyHashMap(int tableSize) {
        if (tableSize < DEFAULT_CAPACITY) {
            tableSize = DEFAULT_CAPACITY;
        }
        table = new Entry[tableSize];
        size = 0;
    }

    public MyHashMap() {
        table = new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(final K key, final V value) {
        if (key == null) {
            putForNullKey(value);
        } else {
            reHashing();
            int hash = key.hashCode();
            int index = getIndex(hash, table.length);
            for (Entry e = table[index]; e != null; e = e.next) {
                Object x;
                if (e.hash == hash
                        && ((x = e.key) == key || key.equals(x))) {
                    e.value = value;
                    return;
                }
            }
            addEntry(key, value, hash, index);
        }
    }

    private void addEntry(K key, V value, int hash, int index) {
        Entry<K, V> next = table[index];
        table[index] = new Entry<K, V>(key, value, hash, next);
        size++;
    }

    private void putForNullKey(V value) {
        Entry<K, V> entry;
        for (entry = table[0]; entry != null; entry = entry.next) {
            if (entry.key == null) {
                entry.value = value;
                return;
            }
        }
        table[0] = new Entry<K, V>(null, value, 0, entry);
        size++;
    }

    private void reHashing() {
        if (size > table.length * LOAD_FACTOR) {
            Entry<K, V>[] newTable = new Entry[table.length * 3 / 2];
            moveTable(newTable);
            this.table = newTable;
        }
    }

    private void moveTable(Entry<K, V>[] dest) {
        Entry<K, V> entry;
        for (int i = 0; i < table.length; i++) {
            entry = table[i];
            if (null == entry) {
                continue;
            }
            while (entry != null) {
                Entry<K, V> next = entry.next;
                int index = getIndex(entry.hash, dest.length);
                entry.next = dest[index];
                dest[index] = entry;
                entry = next;
            }
        }
    }

    private int getIndex(int hash, int length) {
        return hash & (length - 1);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }
        int hash = key.hashCode();
        int index = getIndex(hash, table.length);
        Entry<K, V> entry = table[index];
        K k;
        while (entry != null) {
            if (entry.hash == hash
                    && ((k = entry.key) == key || k.equals(key))) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    private V getForNullKey() {
        Entry<K, V> entry = table[0];
        while (entry != null) {
            if (entry.key == null) {
                return entry.value;
            }
            entry = entry.next;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Entry next;

        public Entry(K key, V value, int hash, Entry next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Entry) {
                Entry o = (Entry) obj;
                return o.getKey().equals(this.key)
                        && o.getValue().equals(this.value);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return hash << 16
                    & (key == null ? 0 : key.hashCode() << 8)
                    & (value == null ? 0 : value.hashCode());
        }
    }
}
