package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private int capacity;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> lastPrev = table[index];
        Node<K, V> newNode = new Node<>(key, value, null);
        if (lastPrev == null) {
            table[index] = newNode;
        } else {
            if (lastPrev.next == null) {
                if (Objects.equals(lastPrev.key, key)) {
                    lastPrev.value = value;
                    return;
                }
                lastPrev.next = newNode;
            } else {
                while (lastPrev.next != null) {
                    if (Objects.equals(lastPrev.key, key)) {
                        lastPrev.value = value;
                        return;
                    }
                    lastPrev = lastPrev.next;
                }
                if (Objects.equals(lastPrev.key, key)) {
                    lastPrev.value = value;
                    return;
                }
                lastPrev.next = newNode;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
        for (;currentNode != null; currentNode = currentNode.next) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(hash(key) % capacity);
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private void resize() {
        capacity = capacity << 1;
        threshold = (int) (capacity * LOAD_FACTOR);
        size = 0;
        Node<K, V>[] oldTable = table;
        Node<K, V>[] newTable = new Node[capacity];
        table = newTable;
        for (Node<K, V> bucket: oldTable) {
            if (bucket != null) {
                while (bucket != null) {
                    put(bucket.key, bucket.value);
                    bucket = bucket.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
