package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private float threshold;
    private int size;
    private Node<K, V>[] table;

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> nextNode;

        Node(int hash, K key, V value, Node<K, V> nextNode) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * table.length);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> newNode = new Node(getBucketIndex(key), key, value, null);
        findNode(newNode, key);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> requiredNode = findNode(null, key);
        return (requiredNode == null) ? null : requiredNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> findNode(Node<K, V> newNode, K key) {
        Node<K, V> currentNode = table[getBucketIndex(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                if (newNode != null) {
                    currentNode.value = newNode.value;
                }
                return currentNode;
            }
            if (currentNode.nextNode == null) {
                currentNode.nextNode = newNode;
                size++;
                return newNode;
            }
            currentNode = currentNode.nextNode;
        }
        table[getBucketIndex(key)] = newNode;
        size++;
        return null;
    }

    private void resize() {
        size = 0;
        threshold *= 2;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.nextNode;
            }
        }
    }

    private int getBucketIndex(K key) {
        return Math.abs(hash(key)) % table.length;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }
}
