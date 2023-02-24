package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size;
    private int currentCapacity;
    private int currentLoadFactor;
    private Node<K, V>[] storage;

    public MyHashMap() {
        currentCapacity = DEFAULT_INITIAL_CAPACITY;
        currentLoadFactor = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        storage = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeed();
        int position = getPosition(key);
        Node<K, V> oldNode = storage[position];
        if (oldNode == null) {
            storage[position] = new Node<>(key, value);
            size++;
        } else {
            Node<K, V> nextNode = oldNode;
            while (nextNode != null) {
                if (Objects.equals(nextNode.key, key)) {
                    nextNode.value = value;
                    break;
                }
                if (nextNode.nextNode == null) {
                    nextNode.nextNode = new Node<>(key, value);
                    size++;
                    return;
                } else {
                    nextNode = nextNode.nextNode;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> resultNode = new Node<>(key, null);
        Node<K, V> currentNode = storage[getPosition(key)];
        if (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                resultNode = currentNode;
            } else {
                while (currentNode != null) {
                    if (Objects.equals(currentNode.key, key)) {
                        resultNode = currentNode;
                        break;
                    }
                    currentNode = currentNode.nextNode;
                }
            }
        }
        return resultNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getPosition(K key) {
        int position = key == null ? 0 : key.hashCode() % currentCapacity;
        return position >= 0 ? position : position * -1;
    }

    private void resizeIfNeed() {
        if (currentLoadFactor == size) {
            currentCapacity = currentCapacity << 1;
            currentLoadFactor = (int) (currentCapacity * DEFAULT_LOAD_FACTOR);
            Node<K, V>[] oldStorage = storage;
            storage = (Node<K, V>[]) new Node[currentCapacity];
            size = 0;
            for (Node<K, V> node : oldStorage) {
                if (node != null) {
                    Node<K, V> nextNode = node;
                    while (nextNode != null) {
                        put(nextNode.key, nextNode.value);
                        nextNode = nextNode.nextNode;
                    }
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
