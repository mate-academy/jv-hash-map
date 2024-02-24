package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_SIZE_MULTIPLAYER = 2;
    private static final int DEFAULT_SIZE = 0;
    private int loadFactor = 12;
    private Node<K, V>[] storage;
    private int size;

    public MyHashMap() {
        storage = new Node[DEFAULT_CAPACITY];
        size = DEFAULT_SIZE;
    }

    @Override
    public void put(K key, V value) {
        if (size == loadFactor) {
            resize();
        }
        size++;
        int hash = key == null ? 0 : key.hashCode();
        int index = Math.abs(hash % storage.length);
        Node<K, V> currentNode = storage[index];
        Node<K, V> newNode = new Node<>(key, value, null);
        if (currentNode == null) {
            storage[index] = newNode;
        } else {
            while (true) {
                if (Objects.equals(key, currentNode.key)) {
                    currentNode.value = value;
                    size--;
                    break;
                } else if (!currentNode.hasNext()) {
                    currentNode.next = newNode;
                    break;
                }
                currentNode = getNextNode(currentNode);
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = key == null ? 0 : key.hashCode();
        int index = Math.abs(hash % storage.length);
        Node<K, V> currentNode = storage[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = getNextNode(currentNode);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static <K, V> Node<K, V> getNextNode(Node<K, V> currentNode) {
        return currentNode.next == null ? null : currentNode.next;
    }

    private void resize() {
        loadFactor = loadFactor * DEFAULT_SIZE_MULTIPLAYER;
        size = DEFAULT_SIZE;
        Node<K, V>[] oldStorage = storage;
        storage = new Node[storage.length * DEFAULT_SIZE_MULTIPLAYER];
        for (Node<K, V> node : oldStorage) {
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

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        private boolean hasNext() {
            return next != null;
        }
    }
}
