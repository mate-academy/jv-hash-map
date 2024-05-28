package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROWTH_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] nodes;
    private int size;
    private int currentCapacity;

    public MyHashMap() {
        nodes = new Node[DEFAULT_CAPACITY];
        currentCapacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        int threshold = (int) (currentCapacity * LOAD_FACTOR);
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        if (nodes[index] == null) {
            nodes[index] = newNode;
            size++;
            return;
        }
        Node<K, V> node = nodes[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        newNode.next = nodes[index];
        nodes[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = nodes[getIndex(key)];
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

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % currentCapacity;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldNodes = nodes;
        currentCapacity *= GROWTH_FACTOR;
        nodes = (Node<K, V>[]) new Node[currentCapacity];
        for (Node<K, V> node : oldNodes) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
