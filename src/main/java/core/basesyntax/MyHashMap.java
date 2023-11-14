package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Bucket<K, V>[] table;
    private int size = 0;

    public MyHashMap() {
        table = new Bucket[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkCapacity();
        Bucket<K, V> bucket = table[getPosition(key)];
        Bucket<K, V> newBucket = new Bucket<>(key, value);
        if (table[getPosition(key)] == null) {
            table[getPosition(key)] = newBucket;
        } else {
            if (Objects.equals(bucket.key, newBucket.key)) {
                bucket.value = newBucket.value;
                return;
            }
            Bucket<K, V> tempBucket = bucket;
            while (tempBucket.next != null) {
                tempBucket = tempBucket.next;
                if (Objects.equals(tempBucket.key, newBucket.key)) {
                    tempBucket.value = newBucket.value;
                    return;
                }
            }
            tempBucket.next = newBucket;
        }
        size++;
    }

    private int getPosition(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void checkCapacity() {
        int capacity = table.length;
        Bucket<K, V> temp;
        if (size > (int) (table.length * LOAD_FACTOR)) {
            capacity = capacity << 1;
            Bucket<K, V>[] oldTable = table;
            table = (Bucket<K, V>[]) new Bucket[capacity];
            size = 0;
            for (Bucket<K, V> kvBucket : oldTable) {
                temp = kvBucket;
                while (temp != null) {
                    put(temp.key, temp.value);
                    temp = temp.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        Bucket<K, V> bucket = table[getPosition(key)];
        while (bucket != null) {
            if (Objects.equals(key, bucket.key)) {
                return bucket.value;
            }
            bucket = bucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Bucket<K, V> {
        private final K key;
        private V value;
        private Bucket<K, V> next;

        private Bucket(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
