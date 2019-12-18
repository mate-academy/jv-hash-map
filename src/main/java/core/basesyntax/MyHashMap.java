package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.5;
    private static final int INITIAL_CAPACITY = 16;
    private int size;
    private Bucket[] data;

    public MyHashMap() {
        data = new Bucket[INITIAL_CAPACITY];
    }

    private static class Bucket<K, V> {
        private K key;
        private V value;

        private Bucket(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        int position = calculatePosition(key);
        if (data[position] == null) {
            data[position] = new Bucket(key, value);
            size++;
            return;
        }
        while (data[position] != null) {
            if (Objects.equals(key, data[position].key)) {
                data[position].value = value;
                return;
            }
            position++;
            if (position >= data.length) {
                position = 0;
            }
        }
        data[position] = new Bucket(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int position = calculatePosition(key);
        while (data[position] != null) {
            if (Objects.equals(key, data[position].key)) {
                return (V) data[position].value;
            }
            position++;
            if (position >= data.length) {
                position = 0;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int calculatePosition(K key) {
        int position;
        if (key == null) {
            position = 0;
        } else {
            position = Math.abs(key.hashCode() % data.length);
        }
        return position;
    }

    private void resize() {
        if (size >= data.length * LOAD_FACTOR) {
            Bucket[] oldData = data;
            data = new Bucket[oldData.length * 2];
            size = 0;
            for (int i = 0; i < oldData.length; i++) {
                if (oldData[i] != null) {
                    put((K) oldData[i].key, (V) oldData[i].value);
                }
            }
        }
    }
}
