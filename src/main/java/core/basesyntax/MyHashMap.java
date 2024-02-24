package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_SIZE = 0;
    private static final int SIMPLE_NUMBER_FOR_HASHCODE = 17;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size = 0;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkTableSize();
        addNodeToTable(key, value);
    }

    @Override
    public V getValue(K key) {
        int indexOfBucket = Math.abs(findIndexOfBucket(getHashCode(key)));
        Node<K, V> currentNode = table[indexOfBucket];
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

    private void checkTableSize() {
        int threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        if (size > threshold) {
            resizeTable();
        }
    }

    private void resizeTable() {
        Node<K, V>[] newTable = new Node[table.length * 2];
        Node<K, V>[] oldTable = table;
        table = newTable;
        size = DEFAULT_SIZE;
        copyNodeFromOldTable(oldTable);
    }

    private void copyNodeFromOldTable(Node<K, V>[] oldTable) {
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private void addNodeToTable(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        int indexOfBucket = Math.abs(findIndexOfBucket(getHashCode(key)));
        Node<K, V> currentNode = table[indexOfBucket];
        if (currentNode != null) {
            while (true) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        } else {
            table[indexOfBucket] = newNode;
            size++;
        }
    }

    private int getHashCode(K key) {
        return (key == null ? 0 : key.hashCode() * SIMPLE_NUMBER_FOR_HASHCODE);
    }

    private int findIndexOfBucket(int hashCode) {
        return hashCode % table.length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
