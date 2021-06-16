package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> newNode = new Node(key, value, null);
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> bucket = table[index];
            while (bucket != null) {
                if (Objects.equals(bucket.key, newNode.key)) {
                    bucket.value = newNode.value;
                    return;
                }
                if (bucket.next == null) {
                    bucket.next = newNode;
                    break;
                }
                bucket = bucket.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentBucket = table[getIndex(key)];
        while (currentBucket != null) {
            if (Objects.equals(key, currentBucket.key)) {
                return currentBucket.value;
            }
            currentBucket = currentBucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * RESIZE_MULTIPLIER];
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> bucket = oldTable[i];
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private int getIndex(K key) {
        int hash = 17;
        hash = 31 * hash + (key == null ? 0 : key.hashCode());
        return Math.abs(hash) % table.length;
    }
}
