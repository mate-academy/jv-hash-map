package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
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

    @Override
    public void put(K key, V value) {
        Node<K, V> node = getNodeByKey(key);
        if (node != null) {
            node.value = value;
            return;
        }
        resize();
        int keyHash = getAbsHash(key);
        Node<K, V> newNode = new Node<>(keyHash, key, value, null);
        addNodeToTable(newNode, table);
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

    @SuppressWarnings("unchecked")
    private void resize() {
        if (size < threshold) {
            return;
        }
        Node<K, V>[] newTable = new Node[table.length * RESIZE_MULTIPLIER];
        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                addNodeToTable(node, newTable);
                node = next;
            }
        }
        table = newTable;
        threshold = threshold * RESIZE_MULTIPLIER;
    }

    private void addNodeToTable(Node<K, V> newNode, Node<K, V>[] givenTable) {
        int bucket = newNode.hash % givenTable.length;
        newNode.next = givenTable[bucket];
        givenTable[bucket] = newNode;
    }

    private Node<K, V> getNodeByKey(K key) {
        int bucket = getAbsHash(key) % table.length;
        Node<K, V> node = table[bucket];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private int getAbsHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }
}
