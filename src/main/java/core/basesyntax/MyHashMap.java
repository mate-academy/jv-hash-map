package core.basesyntax;

import java.util.Arrays;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] values;

    public MyHashMap() {
        values = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        boolean put = true;
        for (int i = 0; i < size; i++) {
            if (values[i].key != null && values[i].key.equals(key)
                    || key == null && values[i].key == null) {
                values[i].value = value;
                put = false;
            }
        }
        if (put) {
            if (size >= LOAD_FACTOR * values.length) {
                int newSize = values.length * 2;
                values = Arrays.copyOf(values, newSize);
            }
            values[size++] = new Node<>(key, value);
        }

    }

    @Override
    public V getValue(K key) {
        for (int i = 0; i < size; i++) {
            if (values[i].key != null && values[i].key.equals(key)
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

    private static class Node<K, V> {
        private final K key;
        private V value;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
