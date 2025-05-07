package core.basesyntax;

import static java.lang.Math.abs;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_FACTOR = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfFull();
        putInSize(key, value);
    }

    @Override
    public V getValue(K key) {
        int bucketNumber = getBucketNumber(key);
        Node node = table[bucketNumber];
        while (node != null) {
            if (node.key == null && key == null || node.key
                    != null && node.key.equals(key)) {
                return (V) node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    private int getBucketNumber(K key) {
        int bucketNumber = 0;
        if (key != null) {
            bucketNumber = abs(key.hashCode() % table.length);
        }
        return bucketNumber;
    }

    private void resizeIfFull() {
        if (table.length * LOAD_FACTOR > size) {
            return;
        }
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * RESIZE_FACTOR];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                putInSize(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void putInSize(K key, V value) {
        int bucketNumber = getBucketNumber(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[bucketNumber] == null) {
            table[bucketNumber] = newNode;
        } else {
            Node<K, V> currentNode = table[bucketNumber];
            while (currentNode != null) {
                if ((currentNode.key == null && key == null)
                        || (currentNode.key != null && currentNode.key.equals(key))) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            newNode.next = table[bucketNumber].next;
            table[bucketNumber].next = newNode;

        }
        size++;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}

