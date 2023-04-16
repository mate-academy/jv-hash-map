package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkCapacity();
        createAndInsertNewNode(key, value, table);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> nextNode = table[getBucketIndex(key, table.length)];
        while (nextNode != null) {
            if (Objects.equals(nextNode.key, key)) {
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

    private void checkCapacity() {
        if (size < table.length * LOAD_FACTOR) {
            return;
        }
        Node<K, V>[] newTable = new Node[(int) (table.length << 1)];
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

        int index = getBucketIndex(key, currentTable.length);
        if (currentTable[index] == null) {
            size++;
            currentTable[index] = newNode;
            return;
        }
        Node<K, V> currentNode = currentTable[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)
                    && currentNode.hash == (key == null ? 0 : key.hashCode())) {
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

    private int getBucketIndex(K key, int length) {
        return key == null ? 0 : Math.abs(key.hashCode() % length);
    }

    private static class Node<K, V> {
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
