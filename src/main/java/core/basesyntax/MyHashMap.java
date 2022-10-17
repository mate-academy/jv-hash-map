package core.basesyntax;

import java.util.Arrays;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final double LOAD_FACTOR = 0.75;
    static final int INCREASE_FACTOR = 2;
    static final int ZERO_SIZE = 0;

    private Node<K, V>[] buckets;
    private int hashSize;
    private int maxSize;
    private int threshold;

    MyHashMap() {
        maxSize = DEFAULT_CAPACITY;
        buckets = (Node<K, V>[]) new Node[maxSize];
        threshold = (int) (maxSize * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        int keyHash = getKeyHash(key);
        int position = getPosition(keyHash);
        if (buckets[position] == null) {
            buckets[position] = new Node<>(keyHash, key, value, null);
        } else {
            Node<K, V> pointer = buckets[position];
            while (pointer.next != null) {
                if (key == pointer.key || key != null && key.equals(pointer.key)) {
                    pointer.value = value;
                    return;
                }
                pointer = pointer.next;
            }
            if (key == pointer.key || key != null && key.equals(pointer.key)) {
                pointer.value = value;
                return;
            } else {
                pointer.next = new Node<>(keyHash, key, value, null);
            }
        }
        hashSize++;
    }

    @Override
    public V getValue(K key) {
        int keyHash = getKeyHash(key);
        Node<K, V> pointer = buckets[getPosition(keyHash)];
        while (pointer != null) {
            if (key == pointer.key || key != null && key.equals(pointer.key)) {
                return pointer.value;
            }
            pointer = pointer.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return hashSize;
    }

    private void checkSize() {
        if (hashSize >= threshold) {
            resize();
        }
    }

    private void resize() {
        hashSize = ZERO_SIZE;
        int oldSize = maxSize;
        maxSize *= INCREASE_FACTOR;
        threshold = (int) (maxSize * LOAD_FACTOR);
        Node<K, V>[] buffer = Arrays.copyOf(buckets, oldSize);
        buckets = (Node<K, V>[]) new Node[maxSize];
        for (int i = 0; i < buffer.length; i++) {
            while (buffer[i] != null) {
                put(buffer[i].key, buffer[i].value);
                buffer[i] = buffer[i].next;
            }
        }
    }

    private int getPosition(int keyHash) {
        return Math.abs(keyHash) % maxSize;
    }

    private int getKeyHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

}
