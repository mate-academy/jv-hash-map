package core.basesyntax;

import java.util.Arrays;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private MyEntry<K, V>[] values;

    public MyHashMap() {
        values = new MyEntry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        boolean insert = true;
        for (int i = 0; i < size; i++) {
            if ((values[i].key != null && values[i].key.equals(key))
                    || values[i].key == null && key == null) {
                values[i].value = value;
                insert = false;
            }
        }
        if (insert) {
            ensureCap();
            values[size++] = new MyEntry<>(key, value);
            return;
        }
    }

    @Override
    public V getValue(K key) {
        for (int i = 0; i < size; i++) {
            if ((values[i].key != null && values[i].key.equals(key))
                    || values[i].key == null && key == null) {
                return values[i].value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void ensureCap() {
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
