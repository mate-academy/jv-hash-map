package core.basesyntax;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private List<Entry<K, V>>[] table;
    private int size;

    public MyHashMap() {
        this.table = new ArrayList[INITIAL_CAPACITY];
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = getIndex(hash);
        if (table[index] == null) {
            table[index] = new ArrayList<>();
        }
        for (Entry<K, V> entry : table[index]) {
            if (isEqual(entry.getKey(), key)) {
                entry.setValue(value);
                return;
            }
        }
        table[index].add(new Entry<>(key, value));
        size++;
        if (size > table.length * 0.75) {
            rehash();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = getIndex(hash);
        if (table[index] != null) {
            for (Entry<K, V> entry : table[index]) {
                if (isEqual(entry.getKey(), key)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(int hash) {
        return (hash & 0x7FFFFFFF) % table.length;
    }

    private void rehash() {
        List<Entry<K, V>>[] oldTable = table;
        table = new ArrayList[2 * oldTable.length];
        size = 0;
        for (List<Entry<K, V>> bucket : oldTable) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private boolean isEqual(K key1, K key2) {
        if (key1 == null) {
            return key2 == null;
        }
        return key1.equals(key2);
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
