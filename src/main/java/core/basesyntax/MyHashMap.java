package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(newNode.key);
        Node<K, V> nodeFromBucket = table[index];
        if (nodeFromBucket == null) {
            table[index] = newNode;
            size++;
        } else {
            while (nodeFromBucket != null) {
                if (Objects.equals(key, nodeFromBucket.key)) {
                    nodeFromBucket.value = value;
                    return;
                }
                if (nodeFromBucket.next == null) {
                    nodeFromBucket.next = newNode;
                    size++;
                    return;
                }
                nodeFromBucket = nodeFromBucket.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> nodeFromBucket = table[getIndex(key)];

        while (nodeFromBucket != null) {
            if (Objects.equals(key, nodeFromBucket.key)) {
                return nodeFromBucket.value;
            } else {
                nodeFromBucket = nodeFromBucket.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resize() {
        size = 0;
        Node<K, V>[] oldTable;
        int oldCapacity = table.length;
        int newCapacity = oldCapacity << 1;
        int newThreshold = threshold << 1;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        oldTable = table;
        table = newTable;
        threshold = newThreshold;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    public int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
