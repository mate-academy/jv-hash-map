package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

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

    public MyHashMap() {
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node(key, value, null);
        if (table[calculateBucketIndex(key)] == null) {
            table[calculateBucketIndex(key)] = newNode;
            size++;
            return;
        }
        putNode(newNode);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = findNode(key);
        return currentNode == null ? null : currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size < threshold) {
            return;
        }
        size = 0;
        threshold *= 2;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * 2];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int calculateBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private Node<K, V> findNode(K key) {
        Node<K, V> currentNode;
        for (currentNode = table[calculateBucketIndex(key)]; currentNode != null; 
                                                currentNode = currentNode.next) {
            if (Objects.equals(key, currentNode.key)) {
                return currentNode;
            }
        }
        return null;
    }
    
    private void putNode(Node<K, V> newNode) {
        Node<K, V> currentNode;
        for (currentNode = table[calculateBucketIndex(newNode.key)]; currentNode != null;
                                    currentNode = currentNode.next) {
            if (Objects.equals(newNode.key, currentNode.key)) {
                currentNode.value = newNode.value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                return;
            }
        }
    }
}
