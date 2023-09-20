package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size = 0;
    private int threshold;
    private Node<K, V>[] values;

    static class Node<K, V> {
        private Node<K, V> next;
        private final int hash;
        private final K key;
        private V value;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        if (values == null) {
            resize();
        }
        putValue(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        if (isEmpty()) {
            return null;
        }
        int hash = hash(key);
        int index = hash % values.length;
        Node<K, V> currentNode = values[index];
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

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public V remove(K key) {
        int hash = hash(key);
        int index = hash % values.length;
        Node<K, V> currentNode = values[index];
        while (currentNode != null) {
            Node<K, V> nextNode = currentNode.next;
            if (nextNode != null && (Objects.equals(key, nextNode.key))) {
                currentNode.next = nextNode.next;
                return nextNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private void putValue(int hash, K key, V value) {
        int index = hash % values.length;
        Node<K, V> currentNode = values[index];
        if (currentNode == null) {
            values[index] = new Node<>(hash, key, value);
        } else {
            while (true) {
                if (Objects.equals(key, currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    break;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = new Node<>(hash, key, value);
        }
        if (++size >= threshold) {
            resize();
        }
    }

    private void resize() {
        if (values == null) {
            // Initialization
            values = new Node[DEFAULT_SIZE];
            threshold = (int) (DEFAULT_SIZE * LOAD_FACTOR);
            return;
        }
        int newCapacity = values.length << 1;
        final Node<K, V>[] currentValues = values;
        values = new Node[newCapacity];
        threshold = (int) (values.length * LOAD_FACTOR);
        size = 0;
        for (Node<K, V> currentNode : currentValues) {
            if (currentNode != null) {
                putValue(currentNode.hash, currentNode.key, currentNode.value);
                Node<K, V> nextNode = currentNode.next;
                while (nextNode != null) {
                    putValue(nextNode.hash, nextNode.key, nextNode.value);
                    nextNode = nextNode.next;
                }
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }
}
