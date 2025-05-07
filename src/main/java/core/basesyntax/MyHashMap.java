package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_VALUE = 2;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = generateIndex(key);
        if (buckets[index] != null) {
            Node<K, V> oldNode = null;
            for (Node<K, V> i = buckets[index]; i != null; i = i.next) {
                if (Objects.equals(i.key, key)) {
                    i.value = value;
                    return;
                }
                oldNode = i;
            }
            oldNode.next = new Node<>(Objects.hash(key), key, value, null);
        } else {
            buckets[index] = new Node<>(Objects.hash(key), key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = buckets[generateIndex(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int generateIndex(K key) {
        return key == null ? 0 : key.hashCode() & 0xfffffff % buckets.length;
    }

    private void resize() {
        if (size >= buckets.length * LOAD_FACTOR) {
            int newCapacity = buckets.length * RESIZE_VALUE;
            Node<K, V>[] oldArray = buckets;
            buckets = new Node[newCapacity];
            transport(oldArray);
        }
    }

    private void transport(Node<K, V>[] oldBuckets) {
        size = 0;
        for (int i = 0; i < oldBuckets.length; i++) {
            Node<K, V> currentNode = oldBuckets[i];
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

