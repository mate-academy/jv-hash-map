package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final double DEFAULT_LOAD_FACTOR = 0.75f;
    private final int threshold;
    private Node<K, V>[] bucketsList;
    private int size;

    public MyHashMap() {
        bucketsList = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = hesh(key);
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> currentNode = bucketsList[index];
        Node<K, V> prevNode = null;
        if (bucketsList[index] == null) {
            bucketsList[index] = newNode;
        } else {
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, newNode.key)) {
                    currentNode.value = newNode.value;
                    return;
                }
                prevNode = currentNode;
                currentNode = currentNode.next;
            }
            prevNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hesh(key);
        Node<K, V> currentNode = bucketsList[index];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
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

    private void resize() {
        size = 0;
        Node<K, V>[] oldbucketsList = bucketsList;
        bucketsList = new Node[oldbucketsList.length * 2];
        for (Node<K, V> bucket : oldbucketsList) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private int hesh(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % bucketsList.length);
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
