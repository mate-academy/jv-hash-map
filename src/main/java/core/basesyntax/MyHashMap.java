package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_SIZE = 0;
    private static final int SIMPLE_NUMBER_FOR_HASHCODE = 17;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];
    private int size = 0;

    @Override
    public void put(K key, V value) {
        checkTableSize();
        addNodeToTable(key, value);
    }

    @Override
    public V getValue(K key) {
        int keyHashCode = getHashCode(key);
        int indexOfBucket = Math.abs(findBucket(keyHashCode));
        if (table[indexOfBucket] != null) {
            Node<K, V> currentNode = table[indexOfBucket];
            while (true) {
                if (Objects.equals(currentNode.key, key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next != null ? currentNode.next : null;
            }
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
        for (Node<K, V> nodeFromOldTable : oldTable) {
            if (nodeFromOldTable != null) {
                Node<K, V> currentNode = nodeFromOldTable;
                while (true) {
                    put(currentNode.key, currentNode.value);
                    if (currentNode.next == null) {
                        break;
                    }
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private void addNodeToTable(K key, V value) {
        Node<K, V> newNode = new Node<>(getHashCode(key), key, value, null);
        int indexOfBucket = Math.abs(findBucket(newNode.hash));
        if (table[indexOfBucket] != null) {
            Node<K, V> currentNode = table[indexOfBucket];
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
        return (key == null ? 0 : key.hashCode() + SIMPLE_NUMBER_FOR_HASHCODE
                * SIMPLE_NUMBER_FOR_HASHCODE);
    }

    private int findBucket(int hashCode) {
        return hashCode % table.length;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
