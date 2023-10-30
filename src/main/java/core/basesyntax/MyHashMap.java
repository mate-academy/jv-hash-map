package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value);
        size = putNodeAtPosition(newNode, getBucketIndex(key)) ? size + 1 : size;
    }

    @Override
    public V getValue(K key) {
        int position = getBucketIndex(key);
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

    private int getBucketIndex(K key) {
        int hash = key == null ? 0 : key.hashCode();
        return (hash < 0 ? hash * -1 : hash) % table.length;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * CAPACITY_MULTIPLIER];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (Node<K, V> kvNode : oldTable) {
            while (kvNode != null) {
                put(kvNode.key, kvNode.value);
                kvNode = kvNode.next;
            }
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
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
