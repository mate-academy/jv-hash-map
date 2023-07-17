package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private static final int SIZE_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        checkGrow();
        int index = calculateIndex(key);
        if (table[index] == null) {
            table[index] = createNode(key, value); //non exist value
        } else {
            putInBucket(index, key, value);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[calculateIndex(key)];
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

    private Node<K, V> createNode(K key, V value) {
        size++;
        return new Node<>(key, value, getHashForKey(key));
    }

    private void putInBucket(int index, K key, V value) {
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                break;
            }
            if (currentNode.next == null) {
                currentNode.next = createNode(key, value);
                break;
            }
            currentNode = currentNode.next;
        }
    }

    private int calculateIndex(K key) {
        int keyHash = getHashForKey(key);
        return Math.abs(keyHash % table.length);
    }

    private int getHashForKey(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private void checkGrow() {
        if (size == threshold) {
            grow();
        }
    }

    private void grow() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * SIZE_MULTIPLIER];
        threshold *= SIZE_MULTIPLIER;
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }
}
