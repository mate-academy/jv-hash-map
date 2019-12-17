package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPACITY = 16;
    private int size;
    private Bucket[] data;

    public MyHashMap() {
        data = new Bucket[INITIAL_CAPACITY];
    }

    private static class Bucket<K, V> {
        private K key;
        private V value;
        private Bucket next;

        private Bucket(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        int position = calculatePosition(key);
        Bucket localBucket = new Bucket(key, value);
        if (data[position] == null) {
            data[position] = localBucket;
            size++;
            return;
        }
        localBucket = data[position];
        while (localBucket != null) {
            if (Objects.equals(key, localBucket.key)) {
                localBucket.value = value;
                return;
            }
            if (localBucket.next == null) {
                break;
            }
            localBucket = localBucket.next;
        }
        localBucket.next = new Bucket(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int position = calculatePosition(key);
        Bucket localBucket = data[position];
        while (localBucket != null) {
            if (Objects.equals(key, localBucket.key)) {
                return (V) localBucket.value;
            }
            localBucket = localBucket.next;
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
                    Bucket<K, V> tempBucket = oldData[i];
                    while (tempBucket != null) {
                        put(tempBucket.key, tempBucket.value);
                        tempBucket = tempBucket.next;
                    }
                }
            }
        }
    }
}
