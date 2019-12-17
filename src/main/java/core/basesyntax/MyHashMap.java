package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
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
        int position;
        if (key == null) {
            position = 0;
        } else {
            position = Math.abs(key.hashCode() % data.length);
        }
        if (data[position] == null) {
            Bucket newBucket = new Bucket(key, value);
            data[position] = newBucket;
            size++;
            return;
        }
        Bucket localBucket = data[position];
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
        int position;
        if (key == null) {
            position = 0;
        } else {
            position = Math.abs(key.hashCode() % data.length);
        }
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

    private void resize() {
        if (size >= data.length * 0.75) {
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
