package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_START_CAPACITY = 16;
    private static final int INCREASE_CAPACITY_VALUE = 2;
    private int capacity;
    private int size;
    private int threshold;
    private Node<K, V>[] nodes;

    public MyHashMap() {
        this.capacity = DEFAULT_START_CAPACITY;
        this.threshold = (int) (DEFAULT_LOAD_FACTOR * capacity);
        this.nodes = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(key);
        Node<K, V> lastNode = getLastCollisionNode(index);
        if (lastNode == null) {
            nodes[index] = newNode;
            size++;
        } else {
            putNewNode(newNode, nodes[index]);
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> firsNodeOnIndex = nodes[index];
        if (firsNodeOnIndex == null) {
            return null;
        }
        Node<K, V> foundNode = findNestedNode(firsNodeOnIndex, key);
        return foundNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % capacity);
    }

    private Node<K, V> getLastCollisionNode(int index) {
        if (nodes[index] == null) {
            return null;
        }
        Node<K, V> lastNode = nodes[index];
        while (lastNode.next != null) {
            lastNode = lastNode.next;
        }
        return lastNode;
    }

    private Node<K, V> findNestedNode(Node<K, V> nextNode, K key) {
        if (nextNode.next != null && !nextNode.equals(key)) {
            nextNode = findNestedNode(nextNode.next, key);
        }
        return nextNode;
    }

    private void putNewNode(Node<K, V> newNode, Node<K, V> currentNode) {
        K currentNodeKey = currentNode.key;
        if (newNode.equals(currentNodeKey)) {
            currentNode.value = newNode.value;
            return;
        }
        if (currentNode.next == null) {
            currentNode.next = newNode;
            size++;
            return;
        }
        putNewNode(newNode, currentNode.next);
    }

    private void resize() {
        this.capacity = capacity * INCREASE_CAPACITY_VALUE;
        this.threshold = (int) (DEFAULT_LOAD_FACTOR * capacity);
        this.size = 0;
        Node<K, V>[] prevNodes = nodes;
        this.nodes = new Node[capacity];
        for (Node<K, V> node : prevNodes) {
            if (node != null) {
                reassignNodesPositions(node);
            }
        }
    }

    private void reassignNodesPositions(Node<K, V> node) {
        Node<K, V> nextNode = node;
        while (nextNode != null) {
            put(nextNode.key, nextNode.value);
            nextNode = nextNode.next;
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object key) {
            return Objects.equals(key, this.key);
        }
    }
}
