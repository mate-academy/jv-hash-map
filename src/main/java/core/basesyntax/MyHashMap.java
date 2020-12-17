package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K, V>[] nodes;
    private int size;
    private double threshold;

    public MyHashMap() {
        nodes = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = LOAD_FACTOR * DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        int index = getIndexOfBucket(key);
        Node<K, V> node = nodes[index];
        if (node == null) {
            nodes[index] = new Node<>(null, key, value);
            size++;
            return;
        }
        while (node.next != null || Objects.equals(node.key, key)) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        node.next = new Node<>(null, key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        if (size != 0) {
            Node<K, V> node = nodes[getIndexOfBucket(key)];
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int capacity = nodes.length * 2;
        threshold = capacity * LOAD_FACTOR;
        size = 0;
        Node<K, V>[] oldTable = nodes;
        nodes = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> oldNode : oldTable) {
            while (oldNode != null) {
                put(oldNode.key, oldNode.value);
                oldNode = oldNode.next;
            }
        }
    }

    private int getIndexOfBucket(K key) {
        return key != null ? Math.abs(key.hashCode()) % nodes.length : 0;
    }

    private class Node<K, V> {
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
