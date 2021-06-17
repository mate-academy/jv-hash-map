package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private int threshold;
    private int capacity;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        capacity = DEFAULT_CAPACITY;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
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

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
        } else {
            Node<K,V> currentNode = table[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(key, value, null);
                    break;
                }
                currentNode = currentNode.next;
            }
        }
        size++;
        if (size == threshold) {
            resize();
        }
    }

    private void resize() {
        capacity = table.length * CAPACITY_MULTIPLIER;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] newTable = new Node[capacity];
        for (Node<K, V> currentNode : table) {
            Node<K, V> newNode = currentNode;
            while (newNode != null) {
                int newIndex = getIndex(newNode.key);
                if (newTable[newIndex] == null) {
                    newTable[newIndex] = new Node<>(newNode.key, newNode.value, null);
                } else {
                    Node<K, V> node = newTable[newIndex];
                    while (node.next != null) {
                        node = node.next;
                    }
                    node.next = new Node<>(newNode.key, newNode.value, null);
                }
                newNode = newNode.next;
            }
        }
        table = newTable;
    }
}
