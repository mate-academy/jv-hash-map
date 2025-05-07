package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int capacity;
    private Node<K, V>[] container;
    private int size;

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        container = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > capacity * LOAD_FACTOR) {
            resizeContainer();
        }

        Node<K, V> currentNode = container[getIndex(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }

        container[getIndex(key)] = new Node<>(key, value, container[getIndex(key)]);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = container[getIndex(key)];
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

    private void resizeContainer() {
        Node<K, V>[] newContainer = new Node[capacity * 2];

        for (int i = 0; i < container.length; i++) {
            Node<K, V> currentNode = container[i];
            while (currentNode != null) {
                Node<K, V> nextNode = currentNode.next;

                int newIndex = Math.abs(currentNode.key.hashCode()) % (capacity * 2);
                currentNode.next = newContainer[newIndex];
                newContainer[newIndex] = currentNode;

                currentNode = nextNode;
            }
        }

        capacity *= 2;
        container = newContainer;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
