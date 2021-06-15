package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K, V>[] table;
    private int capacity;
    private int size;
    private int threshold;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        if (putVal(key, value, table, getHash(key))) {
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
        Node<K, V> currentNode = table[getHash(key)];
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
        capacity = table.length * 2;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] newNodes = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                int hash = getHash(node.key);
                putVal(node.key, node.value, newNodes, hash);
                node = node.next;
            }
        }
        table = newNodes;
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
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
