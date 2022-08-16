package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75d;
    private double threshold;
    private int capacity;
    private int size;
    private Node<K, V>[] data;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        data = new Node[capacity];
        threshold = capacity * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        Node currentNode;
        Node newNode = new Node<>(key, value, null);
        int index = getBucketByKey(key);
        if (size > threshold) {
            resize();
        }
        if (data[index] == null) {
            data[index] = newNode;
        } else {
            currentNode = data[index];
            while (currentNode.next != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = data[getBucketByKey(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        capacity *= 2;
        Node<K, V>[] oldData = data;
        data = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldData) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold = capacity * LOAD_FACTOR;
    }

    private int getBucketByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
