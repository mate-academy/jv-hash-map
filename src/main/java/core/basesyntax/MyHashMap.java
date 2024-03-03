package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int RESIZE_CONSTANT = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] internalArray;

    public MyHashMap() {
        internalArray = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        growIfArrayFull();
        int hashedKey = hashKey(key);
        int bucketNumber = hashedKey % internalArray.length;
        Node<K, V> newNode = new Node<>(key, value, null, hashedKey);

        addInBucket(newNode, bucketNumber);
    }

    @Override
    public V getValue(K key) {
        int bucketNumber = hashKey(key) % internalArray.length;

        if (internalArray[bucketNumber] != null) {
            Node<K, V> nodeIterator = internalArray[bucketNumber];
            while (nodeIterator != null) {
                if (Objects.equals(nodeIterator.key, key)) {
                    return nodeIterator.value;
                }
                nodeIterator = nodeIterator.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void growIfArrayFull() {
        if (size == threshold) {
            Node<K, V>[] temporaryArray = internalArray;
            int newCapacity = internalArray.length * RESIZE_CONSTANT;
            internalArray = new Node[newCapacity];
            threshold = (int) (newCapacity * LOAD_FACTOR);

            size = 0;
            for (int i = 0; i < temporaryArray.length; i++) {
                Node<K, V> iterator = temporaryArray[i];
                while (iterator != null) {
                    int bucketNumber = iterator.hash % internalArray.length;
                    addInBucket(iterator, bucketNumber);
                    Node<K, V> temp = iterator;
                    iterator = iterator.next;
                    temp.next = null;
                }
            }
        }
    }

    private void addInBucket(Node<K, V> newNode, int bucketNumber) {
        if (internalArray[bucketNumber] == null) {
            internalArray[bucketNumber] = newNode;
            size++;
            return;
        }

        Node<K, V> nodeIterator = internalArray[bucketNumber];
        Node<K, V> penultimateElement = null;
        while (nodeIterator != null) {
            if (Objects.equals(nodeIterator.key, newNode.key)) {
                nodeIterator.value = newNode.value;
                return;
            }
            penultimateElement = nodeIterator;
            nodeIterator = nodeIterator.next;
        }
        penultimateElement.next = newNode;
        size++;
    }

    private int hashKey(K key) {
        return Math.abs(key == null ? 0 : key.hashCode());
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K, V> next, int hash) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = hash;
        }
    }
}
