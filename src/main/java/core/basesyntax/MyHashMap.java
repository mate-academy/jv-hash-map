package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] buckets;
    private int size;
    private int threshold;

    {
        buckets = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
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
        while (currentNode != null) {
            if (Objects.equals(newNode.key, currentNode.key)) {
                currentNode.value = newNode.value;
                return;
            }
            if (currentNode.next == null) {
                break;
            }
            currentNode = currentNode.next;
        }
        currentNode.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        V value = null;
        Node<K, V> currentNode = buckets[getIndexByKey(key)];
        if (currentNode != null) {
            value = currentNode.value;
            while (currentNode.next != null) {
                currentNode = currentNode.next;
                if (Objects.equals(currentNode.key, key)) {
                    value = currentNode.value;
                }
            }
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
            if (currentNode != null) {
                put(currentNode.key, currentNode.value);
                while (currentNode.next != null) {
                    currentNode = currentNode.next;
                    put(currentNode.key, currentNode.value);
                }
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
