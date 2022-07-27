package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_THRESHOLD = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    private static final int GROW_COEFFICIENT = 2;

    private Node<K,V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        threshold = DEFAULT_THRESHOLD;
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            grow();
        }
        int bucketIndex = getBucketByKey(key);
        Node<K, V> currentNode = table[bucketIndex];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value, null);
                size++;
            }
            currentNode = currentNode.next;
        }
        table[bucketIndex] = new Node<>(key, value, null);
        size++;
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

    private void remove(K key) {
        int bucketIndex = getBucketByKey(key);
        Node<K, V> currentNode = table[bucketIndex];
        Node<K, V> prevNode = null;
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                if (prevNode != null) {
                    prevNode.next = currentNode.next;
                } else {
                    table[bucketIndex] = currentNode.next;
                }
                size--;
                return;
            }
            prevNode = currentNode;
            currentNode = currentNode.next;
        }
    }

    private boolean containsKey(K key) {
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

    private boolean containsValue(V value) {
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
