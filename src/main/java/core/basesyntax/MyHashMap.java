package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_MAXIMUM = 2;
    private static final int UNIQUE_NUMBER = 17 * 31;
    private int tableSize = DEFAULT_INITIAL_CAPACITY;
    private Node<K, V>[] table = new Node[tableSize];
    private int size = 0;

    @Override
    public void put(K key, V value) {
        if (size > tableSize * DEFAULT_LOAD_FACTOR) {
            grow();
        }
        int bucketIndex = getHash(key) % tableSize;
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
        int newSize = tableSize * GROW_MAXIMUM;
        Node<K, V>[] newNodeArray = new Node[newSize];
        for (int i = 0; i < tableSize; i++) {
            Node<K, V> currentNode = table[i];
            while (currentNode != null) {
                table[i] = table[i].next;
                currentNode.next = newNodeArray[getHash(currentNode.key) % newSize];
                newNodeArray[getHash(currentNode.key) % newSize] = currentNode;
                currentNode = table[i];
            }
        }
        tableSize = newNodeArray.length;
        table = newNodeArray;
    }

    private Node<K, V> getNode(K key) {
        int hash = getHash(key);
        Node<K, V> testNode = table[hash % tableSize];
        while (testNode != null) {
            if (Objects.equals(key, testNode.key)) {
                return testNode;
            }
            testNode = testNode.next;
        }
        return testNode;
    }

    private int getHash(K key) {
        return (key == null) ? 0 : (UNIQUE_NUMBER + key.hashCode() >>> 1);
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
