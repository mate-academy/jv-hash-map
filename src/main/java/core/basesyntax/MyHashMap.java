package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int capacity = 16;
    private Node<K, V>[] container = new Node[capacity];
    private int size;
    private static final float LOAD_FACTOR = 0.75f;

    private class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = (key == null) ? 0 : Math.abs(key.hashCode());
        }
    }

    @Override
    public void put(K key, V value) {
        if (isContainerLoaded()) {
            resizeContainer();
        }

        Node<K, V> currentBucket = container[getIndexForKey(key)];

        if (!isBucketEmpty(currentBucket)) {
            if (Objects.equals(currentBucket.key, key)) {
                currentBucket.value = value;
                return;
            }

            Node<K, V> currentNode = currentBucket;
            while (currentNode.next != null) {
                currentNode = currentNode.next;

                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }

            }

            currentNode.next = new Node<>(key, value);
            size++;
            return;
        }

        if (key == null) {
            container[0] = new Node<>(null, value);
        } else {
            container[Math.abs(key.hashCode()) % capacity] = new Node<>(key, value);
        }

        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode;

        if (key == null) {
            currentNode = container[0];
        } else {
            currentNode = container[Math.abs(key.hashCode()) % capacity];
        }

        if (isBucketEmpty(currentNode)) {
            return null;
        }

        while (!Objects.equals(currentNode.key, key)) {
            if (currentNode.next == null) {
                return null;
            }

            currentNode = currentNode.next;
        }

        return currentNode.value;
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

                int newIndex = Math.abs(currentNode.hash) % (capacity * 2);
                currentNode.next = newContainer[newIndex];
                newContainer[newIndex] = currentNode;

                currentNode = nextNode;
            }
        }

        capacity *= 2;
        container = newContainer;
    }

    private int getIndexForKey(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private boolean isBucketEmpty(Node<K, V> bucket) {
        return bucket == null;
    }

    private boolean isContainerLoaded() {
        return size > capacity * LOAD_FACTOR;
    }
}
