package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] values;

    public MyHashMap() {
        values = new Node[DEFAULT_SIZE];
    }

    @Override
    public void put(K key, V value) {
        putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        if (isEmpty()) {
            return null;
        }
        int index = getIndex(key);
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
        int index = getIndex(key);
        Node<K, V> currentNode = values[index];
        if (currentNode != null && (Objects.equals(key, currentNode.key))) {
            values[index] = currentNode.next;
            size--;
            return currentNode.value;
        }
        while (currentNode != null) {
            Node<K, V> nextNode = currentNode.next;
            if (nextNode != null && (Objects.equals(key, nextNode.key))) {
                currentNode.next = nextNode.next;
                size--;
                return nextNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private void putValue(K key, V value) {
        if (size >= (int) (values.length * LOAD_FACTOR)) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> currentNode = values[index];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        values[index] = new Node<>(key, value, values[index]);
        size++;
    }

    private void resize() {
        final Node<K, V>[] oldValues = values;
        values = new Node[values.length << 1];
        size = 0;
        for (Node<K, V> currentNode : oldValues) {
            while (currentNode != null) {
                putValue(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getIndex(K key) {
        return hash(key) % values.length;
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private final K key;
        private V value;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
