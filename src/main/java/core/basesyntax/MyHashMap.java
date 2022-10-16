package core.basesyntax;

import java.util.Arrays;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final double LOAD_FACTOR = 0.75;
    private int size;
    private MyEntry<K, V>[] values = new MyEntry[DEFAULT_CAPACITY];

    @Override
    public void put(K key, V value) {
        boolean insert = true;
        for (int i = 0; i < size; i++) {
            if (values[i].key != null) {
                if (values[i].key.equals(key)) {
                    values[i].value = value;
                    insert = false;
                }
            } else if (values[i].key == null && key == null) {
                values[i].value = value;
                insert = false;
            }
        }
        if (insert) {
            ensureCapa();
            values[size++] = new MyEntry<K, V>(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        for (int i = 0; i < size; i++) {
            if (values[i] != null && values[i].key != null) {
                if (values[i].key.equals(key)) {
                    return values[i].value;
                }
            } else if (key == null && values[i].key == null) {
                return values[i].value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void ensureCapa() {
        if (size == values.length * LOAD_FACTOR) {
            int newSize = values.length * 2;
            values = Arrays.copyOf(values, newSize);
        }
    }

    private static class MyEntry<K, V> {
        private final K key;
        private V value;

        public MyEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
