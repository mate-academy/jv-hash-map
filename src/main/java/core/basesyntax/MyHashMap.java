package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private int capacity = 16;
    private int size = 0;
    private double loadFactor = 0.75;
    private int threshold = (int) (capacity * loadFactor);
    @SuppressWarnings("unchecked")
    private Node<K, V>[] bucket = (Node<K, V>[]) new Node[capacity];

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        int index = indexForKey(key);
        Node<K, V> newNode = new Node<>(key, value);

        if (bucket[index] == null) {
            bucket[index] = newNode;
        } else {
            Node<K, V> current = bucket[index];
            while (true) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = indexForKey(key);
        Node<K, V> current = bucket[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        capacity = capacity * 2;
        threshold = (int) (capacity * loadFactor);

        @SuppressWarnings("unchecked")
        Node<K, V>[] newBucket = (Node<K, V>[]) new Node[capacity];

        for (Node<K, V> entry : bucket) {
            while (entry != null) {
                Node<K, V> nextEntry = entry.next;

                int newIndex = indexForKey(entry.key);
                entry.next = newBucket[newIndex];
                newBucket[newIndex] = entry;

                entry = nextEntry;
            }
        }
        bucket = newBucket;
    }

    private int indexForKey(K key) {
        int h = (key == null) ? 0 : key.hashCode();
        return (h & 0x7fffffff) % capacity;
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
