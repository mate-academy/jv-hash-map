package core.basesyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;
    private int tableLength;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        tableLength = INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(getKeyHash(key), key, value, null);
        size = putNodeAtPosition(newNode, getKeyHash(key) % tableLength) ? size + 1 : size;
        if (size > tableLength * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int position = getKeyHash(key) % tableLength;
        Node<K, V> currentNode = table[position];
        if (currentNode == null) {
            return null;
        }
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

    private int getKeyHash(K key) {
        int hash = key == null ? 0 : key.hashCode();
        return hash < 0 ? hash * -1 : hash;
    }

    private void resize() {
        tableLength *= CAPACITY_MULTIPLIER;
        Node<K, V>[] oldTable = table;
        table = new Node[tableLength];
        for (Node<K, V> kvNode : oldTable) {
            if (kvNode != null) {
                addAllNodeByHead(kvNode);
            }
        }
    }

    private void addAllNodeByHead(Node<K, V> head) {
        List<Node<K, V>> nodes = new ArrayList<>();
        Node<K, V> currentNode = head;
        while (currentNode != null) {
            Node<K, V> addedNode = currentNode;
            currentNode = currentNode.next;
            addedNode.next = null;
            nodes.add(addedNode);
        }
        for (Node<K, V> node : nodes) {
            putNodeAtPosition(node, node.hash % tableLength);
        }
    }

    private boolean putNodeAtPosition(Node<K, V> node, int position) {
        if (table[position] == null) {
            table[position] = node;
            return true;
        }
        Node<K, V> currentNode = table[position];
        Node<K, V> prevNode = null;
        while (currentNode != null) {
            prevNode = currentNode;
            if (Objects.equals(node.key, currentNode.key)) {
                currentNode.value = node.value;
                return false;
            }
            currentNode = currentNode.next;
        }
        prevNode.next = node;
        return true;
    }

    private static class Node<K, V> {
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
