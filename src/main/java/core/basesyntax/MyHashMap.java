package core.basesyntax;

import java.util.Arrays;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int capacity;
    private int thredshold;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        thredshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= thredshold) {
            transfer();
        }
        Node<K, V> bucket = new Node<>(key, value, null);
        addEntry(key, value, bucket);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> bucket = table[getHash(key) % table.length];

        while (bucket != null) {
            if (bucket.key == key || bucket.key.equals(key)) {
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

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() + 1);
    }

    private void transfer() {
        Node<K, V>[] copiedArray;
        capacity *= 2;
        thredshold = (int) (capacity * LOAD_FACTOR);
        copiedArray = Arrays.copyOf(table, capacity);
        this.table = new Node[capacity];
        this.size = 0;

        for (int i = 0; i < copiedArray.length; i++) {
            Node<K, V> currentBucket = copiedArray[i];
            while (currentBucket != null) {
                K keyOldTable = currentBucket.key;
                V valueOldTable = currentBucket.value;
                addEntry(keyOldTable, valueOldTable, new Node<>(keyOldTable, valueOldTable, null));
                currentBucket = currentBucket.next;
            }
        }
    }

    private void addEntry(K key, V value, Node<K, V> bucket) {
        int index = getHash(key) % table.length;
        Node<K, V> existing = table[index];
        if (existing == null) {
            table[index] = bucket;
            size++;
        } else {
            while (existing.next != null) {
                if (existing.key == key || existing.key.equals(key)) {
                    existing.value = value;
                    return;
                }
                existing = existing.next;
            }
            if (existing.key == key || existing.key.equals(key)) {
                existing.value = value;
            } else {
                existing.next = bucket;
                size++;
            }
        }
    }

    static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
