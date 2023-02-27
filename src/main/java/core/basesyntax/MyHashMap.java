package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] storage;

    public MyHashMap() {
        storage = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeed();
        int position = getPosition(key);
        Node<K, V> node = storage[position];
        if (node == null) {
            storage[position] = new Node<>(key, value);
            size++;
        } else {
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                if (node.nextNode == null) {
                    node.nextNode = new Node<>(key, value);
                    size++;
                    return;
                }
                node = node.nextNode;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = storage[getPosition(key)];
        if (currentNode != null && Objects.equals(currentNode.key, key)) {
            return currentNode.value;
        } else {
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.nextNode;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getPosition(K key) {
        int position = key == null ? 0 : key.hashCode() % storage.length;
        return Math.abs(position);
    }

    private void resizeIfNeed() {
        if (size >= storage.length * DEFAULT_LOAD_FACTOR) {
            Node<K, V>[] oldStorage = storage;
            storage = new Node[storage.length << 1];
            size = 0;
            for (Node<K, V> node : oldStorage) {
                Node<K, V> nextNode = node;
                while (nextNode != null) {
                    put(nextNode.key, nextNode.value);
                    nextNode = nextNode.nextNode;
                }
            }
        }
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
