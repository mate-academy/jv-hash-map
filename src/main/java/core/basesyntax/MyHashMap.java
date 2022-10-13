package core.basesyntax;

import java.util.Arrays;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final double LOAD_FACTOR = 0.75;

    static class Node<K, V> {
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
        int keyHash = key == null ? 0 : Math.abs(key.hashCode());
        int position = keyHash % maxSize;
        if (buckets[position] == null) {
            buckets[position] = new Node<>(keyHash, key, value, null);
        } else {
            Node<K,V> pointer = buckets[position];
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
            }
            pointer.next = new Node<>(keyHash, key, value, null);
        }
        hashSize++;
    }

    @Override
    public V getValue(K key) {
        int keyHash = key == null ? 0 : key.hashCode();
        int position = Math.abs(keyHash) % maxSize;
        Node<K, V> pointer = buckets[position];
        if (pointer != null) {
            while (pointer != null) {
                if (key == pointer.key || key != null && key.equals(pointer.key)) {
                    return pointer.value;
                }
                pointer = pointer.next;
            }
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
        hashSize = 0;
        int oldSize = maxSize;
        maxSize *= 2;
        threshold = (int) (maxSize * LOAD_FACTOR);
        Node<K, V>[] buffer = Arrays.copyOf(buckets, oldSize);
        buckets = (Node<K, V>[]) new Node[maxSize];
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] != null) {
                while (buffer[i] != null) {
                    put(buffer[i].key, buffer[i].value);
                    buffer[i] = buffer[i].next;
                }
            }
        }
    }
}
