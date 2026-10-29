package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float INITIAL_LOAD_FACTOR = 0.75f;
    private static final int INITIAL_SIZE = 0;

    private int capacity;
    private float loadFactor;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = INITIAL_CAPACITY;
        loadFactor = INITIAL_LOAD_FACTOR;
        threshold = (int) (capacity * loadFactor);
        size = INITIAL_SIZE;
        table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        if (table[index] != null) {
            Node<K, V> newNode = new Node<>(key, value);
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    newNode.next = null;
                    ++size;
                    return;
                }
                currentNode = currentNode.next;
            }
        } else {
            table[index] = new Node<>(key, value);
            ++size;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
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

    private void resize() {
        int oldCap = capacity;
        capacity = capacity * 2;
        threshold = (int) (capacity * loadFactor);
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[capacity];
        int index;
        Node<K, V> currentNode;
        Node<K, V> nextNode;
        for (int i = 0; i < oldCap; ++i) {
            if (table[i] == null) {
                continue;
            }
            currentNode = table[i];
            while (currentNode != null) {
                index = getIndex(currentNode.key);
                nextNode = currentNode.next;
                currentNode.next = newTab[index];
                newTab[index] = currentNode;
                currentNode = nextNode;

            }
        }
        table = newTab;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % capacity;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
