package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity = INITIAL_CAPACITY;
    private int size = 0;
    private Node<K, V>[] elements = new Node[capacity];

    public MyHashMap() {
    }

    @Override
    public void put(K key, V value) {
        int hash = this.hash(key);
        int position = hash % this.capacity;
        Node<K, V> currentNode = this.getNode(key);

        if (currentNode == null) {
            this.elements[position] = new Node<>(hash, key, value, null);
        } else {
            Node<K, V> lastNode = null;

            while (currentNode != null) {
                if (Objects.equals(key, currentNode.key)) {
                    currentNode.value = value;
                    return;
                }

                lastNode = currentNode;
                currentNode = currentNode.next;
            }

            lastNode.next = new Node<>(hash, key, value, null);
        }

        if (++this.size > (int)(capacity * LOAD_FACTOR)) {
            this.resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = this.getNode(key);

        if (currentNode == null) {
            return null;
        }

        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                break;
            }

            currentNode = currentNode.next;
        }

        return currentNode == null ? null : currentNode.value;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    private void resize() {
        int oldCap = this.capacity;
        int newCap = this.capacity << 1;
        Node<K, V>[] newElements = new Node[newCap];

        for (int i = 0; i < oldCap; i++) {
            Node<K, V> node = this.elements[i];

            while (node != null) {
                int newPosition = node.hash % newCap;
                Node<K, V> newNode = new Node<>(node.hash, node.key, node.value, null);

                if (newElements[newPosition] == null) {
                    newElements[newPosition] = newNode;
                } else {
                    Node<K, V> currentNode = newElements[newPosition];
                    Node<K, V> lastNode = null;

                    while (currentNode != null) {
                        lastNode = currentNode;
                        currentNode = currentNode.next;
                    }

                    lastNode.next = newNode;
                }

                node = node.next;
            }
        }

        this.elements = newElements;
        this.capacity = newCap;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode() & Integer.MAX_VALUE;
    }

    private Node<K, V> getNode(K key) {
        return this.elements[this.hash(key) % this.capacity];
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> node) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = node;
        }
    }
}
