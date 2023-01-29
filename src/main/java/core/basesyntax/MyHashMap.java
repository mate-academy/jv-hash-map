package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private double threshold;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.threshold = DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int nodeIndex = getIndex(key);
        if (size == threshold) {
            resize();
        }
        Node<K, V> node = findNode(key, nodeIndex);
        if (node != null) {
            node.value = value;
        } else {
            Node<K, V> newNode = new Node<>(key, value, table[nodeIndex]);
            table[nodeIndex] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int getIndex = getIndex(key);
        Node<K, V> node = findNode(key, getIndex);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] newBucketArray = new Node[table.length * 2];
        threshold = newBucketArray.length * DEFAULT_LOAD_FACTOR;
        size = 0;
        Node<K, V>[] oldBucketArray = table;
        table = newBucketArray;
        for (Node<K, V> node : oldBucketArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.nextNode;
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private Node findNode(K key, int nodeIndex) {
        Node<K, V> node = table[nodeIndex];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node;
            }
            node = node.nextNode;
        }
        return null;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> nextNode;

        private Node(K key, V value, Node<K, V> node) {
            this.key = key;
            this.value = value;
            this.nextNode = node;
        }
    }
}
