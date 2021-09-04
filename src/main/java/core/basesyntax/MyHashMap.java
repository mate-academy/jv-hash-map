package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    private static class Node<K, V> {
        private final K key;
        private Node<K, V> next;
        private V value;

        private Node(V value, K key, Node<K, V> next) {
            this.value = value;
            this.key = key;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int)(DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (threshold == size) {
            resize();
        }
        Node<K, V> newNode = new Node<>(value, key, null);
        if (table[getIndex(key)] == null) {
            table[getIndex(key)] = newNode;
            size++;
        } else {
            Node<K, V> currentNode = table[getIndex(key)];
            while (currentNode.next != null && !Objects.equals(currentNode.key, key)) {
                currentNode = currentNode.next;
            }
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
            } else {
                currentNode.next = newNode;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = findCurrentNode(key);
        return currentNode != null ? currentNode.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(Object key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K, V>[] oldArray = table;
        table = new Node[oldArray.length << 1];
        threshold = (int)(table.length * DEFAULT_LOAD_FACTOR);
        rePlaceNode(oldArray);
    }

    private Node<K, V> findCurrentNode(K key) {
        if (size == 0) {
            return null;
        }
        Node<K, V> currentNode = table[getIndex(key)];
        while (!Objects.equals(currentNode.key, key) && currentNode.next != null) {
            currentNode = currentNode.next;
        }
        return Objects.equals(currentNode.key, key) ? currentNode : null;
    }

    private void rePlaceNode(Node<K, V>[] oldArray) {
        size = 0;
        for (Node<K, V> element : oldArray) {
            Node<K, V> currentNode = element;
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }
}
