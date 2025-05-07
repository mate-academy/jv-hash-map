package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_VALUE = 2;
    private static final int UNIQUE_NUMBER = 527;
    private int tableSize = DEFAULT_INITIAL_CAPACITY;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[tableSize];
    }

    @Override
    public void put(K key, V value) {
        if (size >= tableSize * DEFAULT_LOAD_FACTOR) {
            grow();
        }
        int bucketIndex = getIndex(key, tableSize);
        Node<K, V> currentNode = getNode(key);
        if (currentNode != null) {
            currentNode.value = value;
        } else {
            table[bucketIndex] = new Node<>(key, value, table[bucketIndex]);
            size++;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = getNode(key);
        if (currentNode != null) {
            return currentNode.value;
        }
        return null;
    }

    private void grow() {
        int newSize = tableSize * RESIZE_VALUE;
        Node<K, V>[] newNodeArray = new Node[newSize];
        for (int i = 0; i < tableSize; i++) {
            Node<K, V> currentNode = table[i];
            while (currentNode != null) {
                table[i] = table[i].next;
                currentNode.next = newNodeArray[getIndex(currentNode.key, newSize)];
                newNodeArray[getIndex(currentNode.key, newSize)] = currentNode;
                currentNode = table[i];
            }
        }
        tableSize = newNodeArray.length;
        table = newNodeArray;
    }

    private Node<K, V> getNode(K key) {
        int index = getIndex(key, tableSize);
        Node<K, V> testNode = table[index];
        while (testNode != null) {
            if (Objects.equals(key, testNode.key)) {
                return testNode;
            }
            testNode = testNode.next;
        }
        return testNode;
    }

    private int getIndex(K key, int size) {
        return (key == null) ? 0 : ((UNIQUE_NUMBER + key.hashCode() >>> 1)) % size;
    }

    private class Node<K, V> {
        private V value;
        private final K key;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.value = value;
            this.key = key;
            this.next = next;
        }
    }
}
