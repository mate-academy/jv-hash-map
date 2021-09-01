package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private float threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * table.length);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> newNode = new Node(key, value, null);
        int index = setHash(key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (isKeysIdentical(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.nextNode == null) {
                currentNode.nextNode = newNode;
                size++;
            }
            currentNode = currentNode.nextNode;
        }
    }

    @Override
    public V getValue(K key) {
        int index = setHash(key);
        Node<K, V> newNode = table[index];

        if (index < 0 || index > table.length) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
        }
        while (newNode != null) {
            if (isKeysIdentical(newNode.key, key)) {
                return newNode.value;
            }
            newNode = newNode.nextNode;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        threshold *= (int) (LOAD_FACTOR * table.length);
        Node<K, V>[] oldNode = table;
        table = new Node[table.length * 2];
        for (Node<K, V> node : oldNode) {
            while (node != null) {
                put(node.key, node.value);
                node = node.nextNode;
            }
        }
    }

    private boolean isKeysIdentical(K currentNodeKey, K key) {
        if (Objects.equals(currentNodeKey, key)) {
            return true;
        }
        return false;
    }

    private int setHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        Node(K key, V value, Node<K, V> nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }
}
