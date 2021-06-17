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

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    @Override
    public V getValue(K key) {
        if (table[hash(key)] == null) {
            return null;
        }
        if (table[hash(key)] == key || table[hash(key)] != null && table[hash(key)].equals(key)) {
            return table[hash(key)].value;
        }
        Node<K, V> currentNode = table[hash(key)];
        while (currentNode != null) {
            if (currentNode.key == key || currentNode.key != null && currentNode.key.equals(key)) {
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
        putValue(hash(key), key, value);
        if (size == threshold) {
            resize();
        }
    }

    private void putValue(int hash, K key, V value) {
        if (table[hash] == null) {
            table[hash] = new Node<>(key, value, null);
        } else {
            Node<K,V> currentNode = table[hash];
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
    }

    private void resize() {
        capacity = table.length * CAPACITY_MULTIPLIER;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] newTable = new Node[capacity];
        for (Node<K, V> currentNode : table) {
            if (currentNode == null) {
                continue;
            }
            Node<K, V> newNode = currentNode;
            while (newNode != null) {
                if (newTable[hash(newNode.key)] == null) {
                    newTable[hash(newNode.key)] = new Node<>(newNode.key, newNode.value, null);
                } else {
                    Node<K, V> node = newTable[hash(newNode.key)];
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
