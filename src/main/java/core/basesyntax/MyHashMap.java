package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        checkCapasity();
        createAndInsertNewNode(key, value, table);
    }

    @Override
    public V getValue(K key) {

        Node<K, V> nextNode = table[key == null ? 0 : getBucketIndex(key.hashCode(), table.length)];
        while (nextNode != null) {
            if (nextNode.key == key
                    || nextNode.key != null && nextNode.key.equals(key)) {
                return nextNode.value;
            }
            nextNode = nextNode.nextNode;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkCapasity() {
        if (size < threshold) {
            return;
        }
        Node<K, V>[] newTable = new Node[(int) (table.length << 1)];
        threshold = (int) (newTable.length * LOAD_FACTOR);
        size = 0;
        for (Node<K, V> node : table) {
            while (node != null) {
                createAndInsertNewNode(node.key, node.value, newTable);
                node = node.nextNode;
            }
        }
        table = newTable;
    }

    private void createAndInsertNewNode(K key, V value, Node<K, V>[] currentTable) {
        Node<K, V> newNode = new Node<>(key, value);

        int index = getBucketIndex(newNode.hash, currentTable.length);
        if (currentTable[index] == null) {
            size++;
            currentTable[index] = newNode;
            return;
        }
        Node<K, V> currentNode = currentTable[index];
        while (currentNode != null) {
            if (isNodeEqual(currentNode.key, key,
                    currentNode.hash, key == null ? 0 : key.hashCode())) {
                currentNode.value = value;
                return;
            }
            if (currentNode.nextNode == null) {
                break;
            }
            currentNode = currentNode.nextNode;
        }
        size++;
        currentNode.nextNode = newNode;

    }

    private boolean isNodeEqual(K key1, K key2, int hash1, int hash2) {
        boolean keysEqual = key1 == key2 || key1 != null && key1.equals(key2);
        return keysEqual && hash1 == hash2;
    }

    private int getBucketIndex(int hash, int length) {
        return Math.abs(hash % length);
    }

    private class Node<K, V> {
        private final K key;
        private final int hash;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            if (key == null) {
                this.hash = 0;
            } else {
                this.hash = Objects.hashCode(key);
            }
        }
    }
}
