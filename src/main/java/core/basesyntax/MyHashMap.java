package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float LOAD_FACTOR = 0.75f;
    private int capacity;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        table = (Node<K,V>[])new Node[capacity];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int bucket = hash(key);
        Node<K, V> currentNode = table[bucket];
        if (currentNode == null) {
            table[bucket] = new Node<>(key, value, null);
            size++;
        } else {
            while (currentNode.next != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
            } else {
                currentNode.next = new Node<>(key, value, null);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        if (size == threshold) {
            capacity = capacity << 1;
            size = 0;
            Node<K, V>[] oldTable = table;
            table = (Node<K,V>[])new Node[capacity];
            threshold = (int) (capacity * LOAD_FACTOR);
            transition(oldTable);
        }
    }

    private void transition(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                currentNode = putNode(currentNode);
            }
        }
    }

    private Node<K, V> putNode(Node<K, V> node) {
        Node<K, V> nextNode = node.next;
        node.next = null;
        while (node != null) {
            put(node.key, node.value);
            node = node.next;
        }
        return nextNode;
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
