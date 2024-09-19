package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75f;
    private int defaultCapacity = 16;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[defaultCapacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];

        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }

        addNode(key, value, index);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];

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

    private void putForNullKey(V value) {
        Node<K, V> currentNode = table[0];
        while (currentNode != null) {
            if (currentNode.key == null) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        addNode(null, value, 0);
        size++;
    }

    private void resize() {
        defaultCapacity *= 2;
        threshold = (int) (defaultCapacity * LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[defaultCapacity];
        size = 0;

        for (Node<K, V> headNode : oldTable) {
            while (headNode != null) {
                put(headNode.key, headNode.value);
                headNode = headNode.next;
            }
        }
    }

    private void addNode(K key, V value, int index) {
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
    }

    public int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % defaultCapacity;
    }

    public static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
