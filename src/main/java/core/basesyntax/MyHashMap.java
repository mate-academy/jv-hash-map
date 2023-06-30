package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[getIndexByHash(key)] == null) {
            table[getIndexByHash(key)] = newNode;
        } else {
            Node<K, V> currentNode = table[getIndexByHash(key)];
            while (currentNode != null) {
                if (Objects.equals(key, currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    break;
                }
                currentNode = currentNode.next;
            }
        }
        if (++size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndexByHash(key)];
        while (currentNode != null) {
            if (key == null && currentNode.key == null) {
                return currentNode.value;
            }
            if (currentNode.key != null && currentNode.key.equals(key)
                    || currentNode.key == key) {
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

    private int getIndexByHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int capacity = oldTable.length * 2;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
        rewrite(oldTable);
    }

    private void rewrite(Node<K, V>[] oldTable) {
        for (int a = 0; a < oldTable.length; a++) {
            Node<K, V> oldTableNode = oldTable[a];
            while (oldTableNode != null) {
                put(oldTableNode.key, oldTableNode.value);
                oldTableNode = oldTableNode.next;
            }
        }
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
