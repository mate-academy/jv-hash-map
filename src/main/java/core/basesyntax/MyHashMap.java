package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int currentSize;
    private Node<K, V>[] node;

    public MyHashMap() {
        size = DEFAULT_INITIAL_CAPACITY;
        currentSize = 0;
        node = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> currentNode = node[getIndex(key)];
        if (currentNode == null) {
            node[getIndex(key)] = new Node<>(key, value);
            currentSize++;
        } else if (Objects.equals(currentNode.key, key)) {
            node[getIndex(key)].value = value;
        } else {
            while (currentNode.next != null && !Objects.equals(currentNode.key, key)) {
                currentNode = currentNode.next;
            }
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
            } else {
                currentNode.next = new Node<>(key, value);
                currentSize++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = node[getIndex(key)];
        if (currentNode == null || getIndex(key) > size) {
            return null;
        }
        while (currentNode.next != null && !Objects.equals(currentNode.key, key)) {
            currentNode = currentNode.next;
        }
        if (Objects.equals(currentNode.key, key)) {
            return currentNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return currentSize;
    }

    private void resize() {
        if (currentSize > size * LOAD_FACTOR) {
            size += size;
            Node<K, V>[] oldNode = new Node[size];
            Node<K, V>[] newNode = node;
            node = oldNode;
            currentSize = 0;
            for (Node<K, V> eachNode: newNode) {
                while (eachNode != null) {
                    put(eachNode.key, eachNode.value);
                    eachNode = eachNode.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % size;
    }

    private static <K> int getHashCode(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.hash = getHashCode(key);
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
