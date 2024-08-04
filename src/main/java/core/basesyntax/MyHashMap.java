package core.basesyntax;

import java.util.ArrayList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int MAXIMAL_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size = 0;
    private ArrayList<Entry<K,V>>[]table = new ArrayList[DEFAULT_INITIAL_CAPACITY];
    private float threshold = (table.length * DEFAULT_LOAD_FACTOR);

    @Override
    public void put(K key, V value) {
        int index = hash(key);

        if (size >= threshold) {
            resize();
        }

        if (table[index] == null) {
            table[index] = new ArrayList<>();
        }

        for (Entry<K,V> current : table[index]) {
            if ((key == null && current.getKey() == null)
                    || (key != null && key.equals(current.getKey()))) {
                current.setValue(value);
                return;
            }
        }
        table[index].add(new Entry<>(key, value));
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);

        if (table[index] == null) {
            return null;
        }
        for (Entry<K, V> entry : table[index]) {
            if ((key == null && entry.getKey() == null)
                    || (key != null && key.equals(entry.getKey()))) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null ? 0 : Math.abs(key.hashCode()) % table.length);
    }

    private void resize() {
        final ArrayList<Entry<K,V>>[] oldTable = table;
        if (table.length == MAXIMAL_CAPACITY) {
            return;
        }
        int newCapacity = table.length * 2;

        table = new ArrayList[newCapacity];
        threshold = DEFAULT_LOAD_FACTOR * table.length;
        size = 0;

        for (ArrayList<Entry<K,V>> bucket : oldTable) {
            if (bucket != null) {
                for (Entry<K,V> entry : bucket) {
                    put(entry.getKey(), entry.getValue());
                }
            }
        }
    }
}
