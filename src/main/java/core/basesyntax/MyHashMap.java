package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_COEFFICIENT = 2;
    private int capacity;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        int hash = Objects.hashCode(key);
        Node<K, V> node = new Node<>(hash, key, value, null);
        if (size == capacity * LOAD_FACTOR) {
            resize();
        }
        putNode(node);
    }

    @Override
    public V getValue(K key) {
        int index = Objects.hashCode(key) % capacity;
        if (index < 0) {
            index = -index;
        }
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            } else {
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        size = 0;
        capacity = capacity * RESIZE_COEFFICIENT;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void putNode(Node<K, V> node) {
        int index = node.hash % capacity;
        if (index < 0) {
            index = -index;
        }
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, node.key)) {
                currentNode.value = node.value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = node;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        table[index] = node;
        size++;
    }
}
