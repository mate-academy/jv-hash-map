package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] buckets;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.buckets = new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            grow();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndexByKey(key);
        if (buckets[index] == null) {
            buckets[index] = newNode;
            size++;
            return;
        }
        Node<K, V> currentNode = buckets[index];
        Node<K, V> prevNode = currentNode;
        while (currentNode != null) {
            if (Objects.equals(newNode.key, currentNode.key)) {
                currentNode.value = newNode.value;
                return;
            }
            prevNode = currentNode;
            currentNode = currentNode.next;
        }
        prevNode.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        V value = null;
        Node<K, V> currentNode = buckets[getIndexByKey(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                value = currentNode.value;
                return value;
            }
            currentNode = currentNode.next;
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void grow() {
        size = 0;
        Node<K,V>[] oldBuckets = buckets;
        buckets = new Node[oldBuckets.length * 2];
        for (Node<K, V> currentNode : oldBuckets) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
        threshold = (int) (buckets.length * LOAD_FACTOR);
    }

    private int getIndexByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % buckets.length;
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
