package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public MyHashMap(int capacity) {
        this.buckets = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> entry = buckets[index];
        Node<K, V> prev = findNode(entry, key);
        if (prev != null) {
            prev.value = value;
        } else {
            Node<K, V> newEntry = new Node<>(key, value);
            addToBucket(newEntry, index);
            size++;
        }
        resizeIfNeeded();
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = findNode(buckets[index], key);
        return (node != null) ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> findNode(Node<K, V> entry, K key) {
        while (entry != null) {
            if (Objects.equals(entry.key, key)) {
                return entry;
            }
            entry = entry.next;
        }
        return null;
    }

    private void addToBucket(Node<K, V> newEntry, int index) {
        Node<K, V> entry = buckets[index];
        if (entry != null) {
            while (entry.next != null) {
                entry = entry.next;
            }
            entry.next = newEntry;
        } else {
            buckets[index] = newEntry;
        }
    }

    private int getIndex(K key) {
        return key == null ? getNullKeyIndex() : calculateIndex(key, buckets.length);
    }

    private int getIndex(K key, int capacity) {
        return calculateIndex(key, capacity);
    }

    private int calculateIndex(K key, int capacity) {
        int hashCode = key == null ? 0 : key.hashCode();
        int index = hashCode % capacity;
        return index < 0 ? (index + capacity) % capacity : index;
    }

    private int getNullKeyIndex() {
        return buckets.length - 1;
    }

    private void resizeIfNeeded() {
        if ((float) size / buckets.length >= DEFAULT_LOAD_FACTOR) {
            int newCapacity = buckets.length * 2;
            Node<K, V>[] newBuckets = new Node[newCapacity];
            for (Node<K, V> node : buckets) {
                while (node != null) {
                    Node<K, V> next = node.next;
                    int newIndex = getIndex(node.key, newCapacity);
                    node.next = newBuckets[newIndex];
                    newBuckets[newIndex] = node;
                    node = next;
                }
            }
            buckets = newBuckets;
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
