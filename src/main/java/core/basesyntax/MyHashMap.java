package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int COEFFICIENT_INCREASE = 2;
    private int size;
    private MyNode<K, V>[] table;
    private int threshold;

    public MyHashMap() {
        table = new MyNode[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * table.length);
    }

    @Override
    public void put(K key, V value) {
        if (threshold >= size) {
            int indexBucket = indexForBucket(key);
            if (table[indexBucket] == null) {
                table[indexBucket] = new MyNode<>(key, value, null);
            } else {
                MyNode<K, V> oldBucket = table[indexBucket];
                while (oldBucket != null) {
                    if (Objects.equals(key, oldBucket.key)) {
                        oldBucket.value = value;
                        return;
                    } else if (oldBucket.next == null) {
                        oldBucket.next = new MyNode<>(key, value, null);
                        break;
                    }
                    oldBucket = oldBucket.next;
                }
            }
            size++;
        } else {
            resize();
            put(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        int indexBucket = indexForBucket(key);
        MyNode<K, V> oldBucket = table[indexBucket];
        while (oldBucket != null) {
            if (Objects.equals(key, oldBucket.key)) {
                return oldBucket.value;
            }
            oldBucket = oldBucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class MyNode<K, V> {
        private K key;
        private V value;
        private MyNode<K, V> next;

        public MyNode(K key, V value, MyNode<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int indexForBucket(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        size = 0;
        int newTableLength = table.length * COEFFICIENT_INCREASE;
        threshold = (int) (LOAD_FACTOR * newTableLength);
        MyNode<K, V>[] oldTable = table;
        table = new MyNode[newTableLength];

        for (MyNode<K, V> bucket : oldTable) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }
}
