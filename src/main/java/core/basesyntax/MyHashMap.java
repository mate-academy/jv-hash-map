package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity;
    private int size;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        buckets = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        int index = getHashIndex(key, capacity);
        Node<K, V> current = buckets[index];
        if (current == null) {
            buckets[index] = new Node<K, V>(null, key, value);
            size++;
        } else {
            while (current != null) {
                if (Objects.equals(key,current.key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            buckets[index] = new Node<K, V>(buckets[index], key, value);
            size++;
        }
        if ((float) size / capacity > LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getHashIndex(key, capacity);
        Node<K, V> current = buckets[index];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
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

    private int getHashIndex(K key, int capacity) {
        if (key == null) {
            return 0;
        }
        return (key.hashCode() & Integer.MAX_VALUE) % capacity;
    }

    private void resize() {
        capacity = capacity * 2;
        Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[capacity];

        for (Node<K, V> current : oldBuckets) {
            while (current != null) {
                Node<K, V> next = current.next;
                int index = getHashIndex(current.key, capacity);
                current.next = buckets[index];
                buckets[index] = current;
                current = next;
            }
        }
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;

        private Node(Node<K, V> next, K key, V value) {
            this.next = next;
            this.key = key;
            this.value = value;

        }
    }

}


