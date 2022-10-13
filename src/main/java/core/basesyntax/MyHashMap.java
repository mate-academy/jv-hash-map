package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float LOAD_FACTOR = 0.75f;
    static int size;
    static int capacity = DEFAULT_INITIAL_CAPACITY;

    private final Entry<K, V>[] table;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Entry[DEFAULT_INITIAL_CAPACITY];
    }

    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;

            return o instanceof Map.Entry<?, ?> e
                    && Objects.equals(key, e.getKey())
                    && Objects.equals(value, e.getValue());
        }
    }


    @Override
    public void put(K key, V value) {
        int hash = getIndexByTheKey(key);
        Entry<K, V> e = table[hash];
        /*if (++size > (capacity * LOAD_FACTOR)) {
            resize();
        }*/
        if (e == null) {
            table[hash] = new Entry<>(key, value);
            size++;
        } else {
            while (e.next != null) {
                if ((e.getKey() != null) && (e.getKey().equals(key))) {
                    e.setValue(value);
                    return;
                }
                if (e.getKey() == null && key == null) {
                    e.setValue(value);
                    return;
                }

                e = e.next;
            }
            if ((e.getKey() != null) && (e.getKey().equals(key))) {
                e.setValue(value);
                return;
            }
            if (e.getKey() == null && key == null) {
                e.setValue(value);
                return;
            }
            e.next = new Entry<>(key, value);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int hash = getIndexByTheKey(key);
        Entry<K, V> e = table[hash];
        /*if (key == null && table[0].getKey() == null) {
            return table[0].getValue();
        }*/
        if (e == null) {
            return null;
        }
        while (e != null) {
            if (key == null) {
                if (e.getKey() == null) {
                    return e.getValue();
                }
            } else if (key.equals(e.getKey())) {
                return e.getValue();
            }
            e = e.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    /*
    @SuppressWarnings("unchecked")
    private Entry<K, V>[] resize() {
        Entry<K, V>[]newTable = new Entry[capacity * 2];
        capacity *= 2;

    }

    private void transfer(Entry<K, V>[]newTable) {
        for (Entry<K,V> e: table) {
            if (e != null) {
                while (e.next != null) {
                    Entry<K, V> newEntry = new Entry<>(e.getKey(), e.getValue());
                    int hash = getIndexByTheKey(newEntry.getKey());
                    if (newTable[hash] == null) {
                        newTable[hash] = newEntry;
                    }
                }
            }

        }
    }
    */
    private int getIndexByTheKey(K key) {
        int hash = (key == null) ? 0 : (key.hashCode() % capacity);
        return hash < 0 ? -hash : hash;
    }

    public Entry<K, V> remove(K key) {
        int hash = getIndexByTheKey(key);
        Entry<K, V> e = table[hash];
        if (e == null) {
            return null;
        }
        if (e.getKey() == key) {
            table[hash] = e.next;
            e.next = null;
            return e;
        }
        Entry<K, V> prev = e;
        e = e.next;

        while (e != null) {
            if (e.getKey() == key) {
                prev.next = e.next;
                e.next = null;
                return e;
            }
            prev = e;
            e = e.next;
        }
        return null;
    }
}
