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
        size = 0;
    }

    private class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;
        private int hash;

        private Node(Node<K, V> next, K key, V value, int hash) {
            this.next = next;
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        int index = getHash(key);
        Node<K, V> node = nodes[index];
        if (node == null) {
            nodes[index] = new Node<>(null, key, value, index);
            size++;
        } else {
            while (node.next != null || Objects.equals(node.key, key)) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            node.next = new Node<>(null, key, value, index);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (size != 0) {
            Node<K, V> node = nodes[getHash(key)];
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
        Node<K, V>[] newNodes = nodes;
        nodes = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> newNode : newNodes) {
            while (newNode != null) {
                put(newNode.key, newNode.value);
                newNode = newNode.next;
            }
        }
    }

    private int getHash(K key) {
        if (key != null) {
            return Math.abs(key.hashCode() % 16);
        }
        return 0;
    }
}
