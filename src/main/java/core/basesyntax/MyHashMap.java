package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;
    private float threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int hash = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> currentNode = table[hash];
        Node<K, V> previousNode = null;
        boolean keyExists = false;
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                keyExists = true;
                break;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        if (keyExists) {
            currentNode.value = value;
        } else {
            if (previousNode == null) {
                table[hash] = newNode;
            } else {
                previousNode.next = newNode;
            }
            size++;
        }

        currentNode = table[hash];
        while (currentNode.next != null && !Objects.equals(key,currentNode.key)) {
            currentNode = currentNode.next;
        }
        if (Objects.equals(key, currentNode.key)) {
            currentNode.value = value;
            return;
        }
        currentNode.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
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

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        int newLength = table.length * GROW_FACTOR;
        threshold = newLength * DEFAULT_LOAD_FACTOR;
        Node<K, V>[] oldTable = table;
        table = new Node[newLength];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
