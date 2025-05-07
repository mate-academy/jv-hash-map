package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;
    private Node<K, V>[] nodesTable;
    private int size;

    public MyHashMap() {
        this.nodesTable = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = getNodeByKey(key);
        if (node != null) {
            node.value = value;
            return;
        }
        resize();
        Node<K, V> newNode = new Node<>(key, value, null);
        addNodeToTable(newNode, nodesTable);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNodeByKey(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size < nodesTable.length * DEFAULT_LOAD_FACTOR) {
            return;
        }
        Node<K, V>[] newTable = new Node[nodesTable.length * RESIZE_MULTIPLIER];
        for (Node<K, V> node : nodesTable) {
            while (node != null) {
                Node<K, V> next = node.next;
                addNodeToTable(node, newTable);
                node = next;
            }
        }
        nodesTable = newTable;
    }

    private void addNodeToTable(Node<K, V> newNode, Node<K, V>[] givenTable) {
        int bucket = getBucketIndex(newNode.key, givenTable.length);
        newNode.next = givenTable[bucket];
        givenTable[bucket] = newNode;
    }

    private Node<K, V> getNodeByKey(K key) {
        int bucket = getBucketIndex(key, nodesTable.length);
        Node<K, V> node = nodesTable[bucket];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private int getBucketIndex(K key, int tableLength) {
        return key == null ? 0 : Math.abs(key.hashCode() % tableLength);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
