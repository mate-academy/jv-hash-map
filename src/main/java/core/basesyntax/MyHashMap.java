package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Entry<K, V>[] table;
    private int size = 0;
    private Entry<K, V> nullKeyEntry;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Entry<K, V>[]) new Entry[INITIAL_CAPACITY];
    }
    
    private static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            if (nullKeyEntry == null) {
                nullKeyEntry = new Entry<>(null, value,null);
                size++;
            } else {
                nullKeyEntry.value = value;
            }
            return;
        }
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.key.equals(key)) {
                e.value = value;
                return;
            }
        }

        addEntry(hash, key, value,index);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return nullKeyEntry == null ? null : nullKeyEntry.value;
        }
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.key.equals(key)) {
                return e.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addEntry(int hash, K key, V value, int index) {
        if (size >= table.length * LOAD_FACTOR) {
            resize(2 * table.length);
            index = indexFor(hash, table.length);
        }

        Entry<K, V> e = table[index];
        table[index] = new Entry<>(key, value, e);
        size++;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        table = (Entry<K, V>[]) new Entry[newCapacity];

        for (Entry<K, V> e : oldTable) {
            while (e != null) {
                Entry<K, V> next = e.next;
                int index = indexFor(hash(e.key), newCapacity);
                e.next = table[index];
                table[index] = e;
                e = next;
            }
        }
    }

    private int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int indexFor(int hash, int length) {
        return hash % length;
    }
}
