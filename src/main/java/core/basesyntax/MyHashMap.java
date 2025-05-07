package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_INCREASE_NUMBER = 2;
    private static final int FIRST_PRIME_NUMBER = 31;
    private static final int SECOND_PRIME_NUMBER = 17;

    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        int bucketPosition = getBucketPosition(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> bucketNode = table[bucketPosition];
        if (bucketNode == null) {
            table[bucketPosition] = newNode;
        } else {
            Node<K, V> previousNode = null;
            while (bucketNode != null) {
                if (Objects.equals(key, bucketNode.key)) {
                    bucketNode.value = value;
                    return;
                }
                previousNode = bucketNode;
                bucketNode = bucketNode.next;
            }
            previousNode.next = newNode;
        }
        size++;
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int bucketPosition = getBucketPosition(key);
        Node<K, V> currentNode = table[bucketPosition];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
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

    private void resize() {
        int tableCapacity = table.length * RESIZE_INCREASE_NUMBER;
        Node<K, V>[] newTable = new Node[tableCapacity];
        threshold = (int) (DEFAULT_LOAD_FACTOR * tableCapacity);
        transferToNewTable(newTable);

    }

    private void transferToNewTable(Node<K, V>[] newTable) {
        for (Node<K, V> currentNode : table) {
            while (currentNode != null) {
                int newBucketPosition = getBucketPosition(currentNode.key);
                Node<K, V> nextNode = currentNode.next;
                currentNode.next = newTable[newBucketPosition];
                newTable[newBucketPosition] = currentNode;
                currentNode = nextNode;
            }
            table = newTable;
        }
    }

    private int getBucketPosition(K key) {
        return (key == null) ? 0 : getKeyHashCode(key) % table.length;
    }

    private int getKeyHashCode(K key) {
        return Math.abs(FIRST_PRIME_NUMBER * SECOND_PRIME_NUMBER + key.hashCode());
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
