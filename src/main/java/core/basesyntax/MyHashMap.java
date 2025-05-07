package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int INCREASE_IN_TWO_TIMES = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = calculateIndexByHashcode(key);
        if (table[index] == null) {
            putOnEmptyBucket(key, value, index);
        } else {
            putWithCollision(key, value, index);
        }
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndexByHashcode(key);
        Node<K, V> nodeInBucket = table[index];
        while (nodeInBucket != null) {
            if (Objects.equals(key, nodeInBucket.key)) {
                return nodeInBucket.value;
            }
            nodeInBucket = nodeInBucket.nextNode;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value, Node<K, V> nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }

    public int calculateIndexByHashcode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    public void putOnEmptyBucket(K key, V value, int index) {
        Node<K,V> newNode = new Node<>(key, value, null);
        table[index] = newNode;
        size++;
    }

    public void putWithCollision(K key, V value, int index) {
        Node<K,V> nodeInBucket = table[index];

        while (nodeInBucket != null) {
            if (key == nodeInBucket.key || key != null && key.equals(nodeInBucket.key)) {
                nodeInBucket.value = value;
                return;
            }
            if (nodeInBucket.nextNode == null) {
                nodeInBucket.nextNode = new Node<>(key, value, null);
                size++;
                return;
            }
            nodeInBucket = nodeInBucket.nextNode;
        }
    }

    public void resize() {
        size = 0;
        Node<K, V>[] copyTable = table;
        table = new Node[table.length * INCREASE_IN_TWO_TIMES];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> nodeInBucket : copyTable) {
            while (nodeInBucket != null) {
                put(nodeInBucket.key, nodeInBucket.value);
                nodeInBucket = nodeInBucket.nextNode;
            }
        }
    }
}
