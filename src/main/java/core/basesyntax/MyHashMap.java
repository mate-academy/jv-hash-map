package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final double LOAD_FACTOR = 0.75d;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = createStartTable();
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> existingNode = getNode(key);
        if (existingNode != null) {
            existingNode.value = value;
            return;
        }
        double currentFactor = ((double) size / table.length);
        if (currentFactor >= LOAD_FACTOR) {
            resize();
        }
        int index = calculateBucketPosition(key);
        Node<K, V> entryNode = new Node<>(key, value, null);
        if (table[calculateBucketPosition(key)] != null) {
            entryNode.next = table[index];
        }
        table[index] = entryNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> targetNode = getNode(key);
        return targetNode == null ? null : targetNode.getValue();
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
    }

    private Node<K, V> getNode(K key) {
        int index = calculateBucketPosition(key);
        Node<K, V> node = table[index];
        if (node != null) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
            while (node.next != null) {
                node = node.next;
                if (Objects.equals(node.key, key)) {
                    return node;
                }
            }
        }
        return null;
    }

    private Node<K, V>[] createStartTable() {
        return (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
    }

    private void resize() {
        int newLength = table.length * GROW_FACTOR;
        Node<K, V>[] oldBucketsTmp = table;
        table = (Node<K,V>[]) new Node[newLength];
        size = 0;
        transfer(oldBucketsTmp);

    }

    private void transfer(Node<K, V>[] newTable) {
        for (Node<K, V> kvNode : newTable) {
            Node<K, V> node = kvNode;
            if (node != null) {
                put(node.key, node.value);
                while (node.next != null) {
                    node = node.next;
                    put(node.key, node.value);
                }
            }
        }
    }

    private int calculateBucketPosition(K key) {
        int keyHashCode = key != null ? key.hashCode() : 0;
        return Math.abs((keyHashCode % table.length) - 1);
    }
}
