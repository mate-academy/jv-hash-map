package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final double LOAD_FACTOR = 0.75d;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> existingNode = getNode(key);
        if (existingNode != null) {
            existingNode.value = value;
            return;
        }
        double currentFactor = ((double) size / table.length);
        if (currentFactor >= LOAD_FACTOR) {
            resize();
        }
        int index = calculateBucketPosition(key);
        Node<K, V> entryNode = new Node<>(key, value, null);
        if (table[index] != null) {
            entryNode.next = table[index];
        }
        table[index] = entryNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> targetNode = getNode(key);
        return targetNode == null ? null : targetNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNode(K key) {
        int index = calculateBucketPosition(key);
        Node<K, V> node = table[index];
        if (node != null) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
            while (node.next != null) {
                node = node.next;
                if (Objects.equals(node.key, key)) {
                    return node;
                }
            }
        }
        return null;
    }

    private void resize() {
        int newLength = table.length * GROW_FACTOR;
        Node<K, V>[] oldBucketsTmp = table;
        table = (Node<K,V>[]) new Node[newLength];
        size = 0;
        transfer(oldBucketsTmp);

    }

    private void transfer(Node<K, V>[] newTable) {
        for (Node<K, V> kvNode : newTable) {
            while (kvNode != null) {
                put(kvNode.key, kvNode.value);
                kvNode = kvNode.next;
            }
        }
    }

    private int calculateBucketPosition(K key) {
        int keyHashCode = key != null ? key.hashCode() : 0;
        return Math.abs((keyHashCode % table.length) - 1);
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
