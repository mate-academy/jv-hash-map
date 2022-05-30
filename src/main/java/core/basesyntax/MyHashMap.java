package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final int RESIZE_VALUE = 2;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K,V>[] data;
    private int size;

    public MyHashMap() {
        data = new Node[DEFAULT_SIZE];
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 == data.length * LOAD_FACTOR) {
            resize();
        }
        Node<K,V> newNode = new Node<>(key, value, null);
        int index = getIndex(key);
        setAtArray(index, newNode);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> currentBucket = data[index];
        while (currentBucket != null) {
            if (Objects.equals(currentBucket.key, key)) {
                return currentBucket.value;
            }
            currentBucket = currentBucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % data.length;
    }

    private void setAtArray(int index, Node<K,V> newNode) {
        Node<K,V> currentBucket = data[index];
        if (currentBucket == null) {
            data[index] = newNode;
            size++;
        } else {
            setAtList(data[index], newNode);
        }
    }

    private void setAtList(Node<K,V> currentNode, Node<K,V> newNode) {
        do {
            if (Objects.equals(currentNode.key, newNode.key)) {
                currentNode.value = newNode.value;
                return;
            } else if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                return;
            }
            currentNode = currentNode.next;
        } while (currentNode != null);
    }

    private void resize() {
        size = 0;
        Node<K,V>[] oldDate = data;
        data = new Node[oldDate.length * RESIZE_VALUE];
        for (Node<K,V> currentBucket: oldDate) {
            while (currentBucket != null) {
                put(currentBucket.key, currentBucket.value);
                currentBucket = currentBucket.next;
            }
        }
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
