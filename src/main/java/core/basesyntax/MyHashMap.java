package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private int size;
    private Node<K, V>[] hashTable;
    private int threshold;

    public MyHashMap() {
        this.hashTable = new Node[INITIAL_CAPACITY];
        this.threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> ifNodeExistForKey = returnNodeOfKey(key);
        if (ifNodeExistForKey != null) {
            ifNodeExistForKey.value = value;
            return;
        }
        putWithoutCheckKeyContaince(key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> ifNodeExistForKey = returnNodeOfKey(key);
        if (ifNodeExistForKey != null) {
            return ifNodeExistForKey.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> prev, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private int getIndex(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % hashTable.length);
    }

    private Node<K, V> lastAddedNodeInBucket(Node<K, V> previousNode, K key, V value) {
        Node<K, V> head = null;
        Node<K, V> tail = null;
        final Node<K, V> pointer = previousNode;
        final Node<K, V> newNode = new Node<>(key, value, pointer, null);
        tail = newNode;
        if (pointer == null) {
            head = newNode;
        } else {
            pointer.next = newNode;
        }
        return tail;
    }

    private void putWithoutCheckKeyContaince(K key, V value) {
        int indexOfBucket = getIndex(key);
        Node<K, V> nodeInBucket = hashTable[indexOfBucket];
        Node<K, V> currentNodeInBucket = lastAddedNodeInBucket(nodeInBucket, key, value);
        hashTable[indexOfBucket] = currentNodeInBucket;
        size++;
        if (size >= threshold) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = hashTable.length * CAPACITY_MULTIPLIER;
        final Node<K, V>[] oldHashTable = hashTable;
        hashTable = new Node[newCapacity];
        threshold = (int)(newCapacity * LOAD_FACTOR);
        size = 0;
        for (int i = 0; i < oldHashTable.length; i++) {
            Node<K, V> goalNode = oldHashTable[i];
            while (goalNode != null) {
                putWithoutCheckKeyContaince(goalNode.key, goalNode.value);
                goalNode = goalNode.prev;
            }
        }
    }

    private Node<K, V> returnNodeOfKey(K key) {
        int indexOfKeyBucket = getIndex(key);
        Node<K, V> goalNode = hashTable[indexOfKeyBucket];
        while (goalNode != null) {
            if (Objects.equals(key, goalNode.key)) {
                return goalNode;
            }
            goalNode = goalNode.prev;
        }
        return null;
    }
}
