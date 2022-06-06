package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_LENGTH = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int INCREASE_COEFFICIENT = 2;
    private Node<K, V>[] array;
    private int size;
    private int threshold;

    public MyHashMap() {
        array = new Node[DEFAULT_LENGTH];
        threshold = (int) (array.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        increase();
        insert(key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = array[getIndex(key)];
        if (currentNode != null) {
            while (currentNode.next != null && !Objects.equals(key, currentNode.key)) {
                currentNode = currentNode.next;
            }
            return currentNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % array.length;
    }

    private void insert(K key, V value) {
        final int index = getIndex(key);
        final Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> currentNode = array[index];
        if (currentNode == null) {
            array[index] = newNode;
            size++;
            return;
        }
        while (currentNode.next != null && !Objects.equals(key, currentNode.key)) {
            currentNode = currentNode.next;
        }
        if (Objects.equals(key, currentNode.key)) {
            currentNode.key = key;
            currentNode.value = value;
            return;
        }
        currentNode.next = newNode;
        size++;
    }

    private void increase() {
        if (size >= threshold) {
            Node<K, V>[] buffer = array;
            array = new Node[array.length * INCREASE_COEFFICIENT];
            size = 0;
            for (Node<K, V> bucket : buffer) {
                while (bucket != null) {
                    insert(bucket.key, bucket.value);
                    bucket = bucket.next;
                }
            }
            threshold = (int) (array.length * DEFAULT_LOAD_FACTOR);
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
