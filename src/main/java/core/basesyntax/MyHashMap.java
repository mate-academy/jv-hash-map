package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;

    private Node<K, V>[] nodes;
    private int capacity;
    private int size;
    private int threshold;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        nodes = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        if (putVal(key, value, nodes, getHash(key))) {
            size++;
        }
    }

    private boolean putVal(K key, V value, Node<K, V>[] nodes, int hash) {
        if (isNullNode(nodes[hash])) {
            nodes[hash] = new Node<>(hash, key, value, null);
            return true;
        }

        Node<K, V> currentNode = nodes[hash];
        while (!Objects.equals(currentNode.key, key)) {
            if (isNullNode(currentNode.next)) {
                currentNode.next = new Node<>(hash, key, value, null);
                return true;
            }
            currentNode = currentNode.next;
        }
        currentNode.value = value;
        return false;
    }

    private boolean isNullNode(Node<K, V> node) {
        return node == null;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = nodes[getHash(key)];

        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = nodes.length * 2;
        int newThreshold = (int) (newCapacity * LOAD_FACTOR);

        Node<K, V>[] newNodes = (Node<K, V>[]) new Node[newCapacity];
        capacity = newCapacity;
        threshold = newThreshold;

        for (Node<K, V> node : nodes) {
            if (node == null) {
                continue;
            }
            Node<K, V> copy = node;
            while (copy != null) {
                int hash = getHash(copy.key);
                putVal(copy.key, copy.value, newNodes, hash);
                copy = copy.next;
            }
        }

        nodes = newNodes;
    }

    private int getHash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % capacity);
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
