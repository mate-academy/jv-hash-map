package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int MULTIPLIER_CAPACITY_AND_THRESHOLD = 2;
    private int threshold;
    private int capacity;
    private int size;
    private Node<K, V>[] nodes;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
        nodes = new Node[capacity];
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            increaseArray();
        }
        Node<K, V> putNode = new Node<>(key, value, null);
        int position = hash(key);
        Node<K, V> positionNode = nodes[position];
        if (positionNode == null) {
            nodes[position] = putNode;
            size++;
            return;
        }
        Node<K, V> tempNode = positionNode;
        while (positionNode != null) {
            if (Objects.equals(positionNode.key, key)) {
                positionNode.value = value;
                return;
            }
            tempNode = positionNode;
            positionNode = positionNode.next;
        }
        tempNode.next = putNode;
        size++;
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode() % capacity;
        return hash > 0 ? hash : -hash;
    }

    private void increaseArray() {
        size = 0;
        threshold *= MULTIPLIER_CAPACITY_AND_THRESHOLD;
        capacity *= MULTIPLIER_CAPACITY_AND_THRESHOLD;
        Node<K, V>[] tempArray = nodes;
        nodes = new Node[capacity];
        for (Node<K, V> node : tempArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> getNode = nodes[hash(key)];
        while (getNode != null) {
            if (Objects.equals(key, getNode.key)) {
                return getNode.value;
            }
            getNode = getNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
