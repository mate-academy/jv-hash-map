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
        int index = getIndex(newNode.getKey());
        Node<K, V> nodeFromBucket = table[index];
        if (nodeFromBucket == null) {
            table[index] = newNode;
            size++;
        } else {
            while (nodeFromBucket != null) {
                if (Objects.equals(key, nodeFromBucket.getKey())) {
                    nodeFromBucket.setValue(value);
                    return;
                }
                if (nodeFromBucket.getNext() == null) {
                    nodeFromBucket.setNext(newNode);
                    size++;
                    return;
                }
                nodeFromBucket = nodeFromBucket.getNext();
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> nodeFromBucket = table[getIndex(key)];

        while (nodeFromBucket != null) {
            if (key != null && key.equals(nodeFromBucket.getKey())
                    || key == nodeFromBucket.getKey()) {
                return nodeFromBucket.getValue();
            } else {
                nodeFromBucket = nodeFromBucket.getNext();
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable;
        int capacityOld = table.length;
        int capacityNew = capacityOld << 1;
        int thresholdNew = threshold << 1;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacityNew];
        oldTable = table;
        table = newTable;
        threshold = thresholdNew;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.getKey(), node.getValue());
                node = node.getNext();
            }
        }
    }

    private int getIndex(K key) {
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

        public void setKey(K key) {
            this.key = key;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Node<K, V> getNext() {
            return next;
        }
    }
}
