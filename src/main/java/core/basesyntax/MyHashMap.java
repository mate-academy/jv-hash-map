package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int currentCapacity = DEFAULT_CAPACITY;
    private Node<K, V>[] nodeArray;
    private int size;

    public MyHashMap() {
        nodeArray = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = generateIndex(key, currentCapacity);
        Node<K, V> currentNode = nodeArray[index];
        Node<K, V> lastNode = null;
        if (currentNode != null) {
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    break;
                }
                lastNode = currentNode;
                currentNode = currentNode.next;
            }
            if (Objects.equals(currentNode, null)) {
                lastNode.next = new Node<>(Objects.hash(key), key, value, null);
                size++;
            }
        } else {
            nodeArray[index] = new Node<>(Objects.hash(key), key, value, null);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = nodeArray[generateIndex(key, currentCapacity)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
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

    private int generateIndex(K key, int currentCapacity) {
        return key == null ? 0 : key.hashCode() & 0xfffffff % currentCapacity;
    }

    private void resize() {
        if (size >= currentCapacity * LOAD_FACTOR) {
            currentCapacity = currentCapacity * 2;
            Node<K, V>[] oldArray = nodeArray;
            nodeArray = new Node[currentCapacity];
            transport(oldArray);
        }
    }

    private void transport(Node<K, V>[] from) {
        size = 0;
        for (int i = 0; i < from.length; i++) {
            Node<K, V> currentNode = from[i];
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

