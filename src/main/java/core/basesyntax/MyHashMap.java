package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_THRESHOLD = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    private static final int GROW_COEFFICIENT = 2;

    private Node<K,V>[] table;
    private int threshold;
    private int size = 0;

    public MyHashMap() {
        this.threshold = DEFAULT_THRESHOLD;
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getBucketByKey(key);
        if (size >= threshold) {
            grow();
        }
        Node<K, V> currentNode = table[bucketIndex];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                putValueInExistingNode(currentNode, key, value);
                return;
            }
            if (currentNode.next == null) {
                putFirstNodeInBucket(currentNode, key, value);
                return;
            }
            currentNode = currentNode.next;
        }
        putNodeInBucket(key, value, bucketIndex);
    }

    private void putNodeInBucket(K key, V value, int bucketIndex) {
        table[bucketIndex] = new Node<>(key, value, null);
        size++;
    }

    private void putFirstNodeInBucket(Node<K, V> node, K key, V value) {
        node.next = new Node<>(key, value, null);
        size++;
    }

    private void putValueInExistingNode(Node<K, V> node, K key, V value) {
        node.value = value;
    }

    private void grow() {
        size = 0;
        Node<K, V>[] bufferTable = table;
        table = new Node[table.length * GROW_COEFFICIENT];
        for (Node<K, V> node : bufferTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold *= GROW_COEFFICIENT;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketByKey(key);
        Node<K, V> currentNode = table[bucketIndex];
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

    @Override
    public boolean containsKey(K key) {
        int bucketIndex = getBucketByKey(key);
        Node<K, V> currentNode = table[bucketIndex];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                return true;
            }
            currentNode = currentNode.next;
        }
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        for (Node<K, V> node : table) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                if (Objects.equals(value, currentNode.value)) {
                    return true;
                }
                currentNode = currentNode.next;
            }
        }
        return false;
    }

    @Override
    public void remove(K key) {
        int bucketIndex = getBucketByKey(key);
        Node<K, V> currentNode = table[bucketIndex];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                table[bucketIndex] = null;
                size--;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    private int getBucketByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
