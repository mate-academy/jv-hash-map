package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private MyNode<K, V>[] table;
    private int threshold;

    public MyHashMap() {
        table = new MyNode[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * table.length);
    }

    @Override
    public void put(K key, V value) {
        if (threshold < size) {
            resize();
        }

        if (threshold >= size) {
            int indexBucket = hashCode(key, table.length);
            if (table[indexBucket] == null) {
                MyNode<K, V> bucket = new MyNode<>(key, value, null);
                table[indexBucket] = bucket;
            } else {
                MyNode<K, V> oldBucket = table[indexBucket];
                while (oldBucket != null) {
                    if (Objects.equals(key, oldBucket.key)) {
                        oldBucket.value = value;
                        return;
                    } else if (oldBucket.next == null) {
                        MyNode<K, V> bucket = new MyNode<>(key, value, null);
                        oldBucket.next = bucket;
                        break;
                    }
                    oldBucket = oldBucket.next;
                }
            }
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int indexBucket = hashCode(key, table.length);
        if (table[indexBucket] == null) {
            return null;
        }
        if (Objects.equals(key, table[indexBucket].key)) {
            return table[indexBucket].value;
        } else {
            MyNode<K, V> oldBucket = table[indexBucket];
            while (oldBucket != null) {
                if (Objects.equals(key, oldBucket.key)) {
                    return oldBucket.value;
                }
                oldBucket = oldBucket.next;
            }
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

    private int hashCode(Object key, int length) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % length);
    }

    private void resize() {
        size = 0;
        int newTableLength = table.length * 2;
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
