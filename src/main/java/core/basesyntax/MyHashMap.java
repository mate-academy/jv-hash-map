package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> currentNode = table[getIndexByKey(key)];
        if (currentNode == null) {
            table[getIndexByKey(key)] = new Node<>(key, value);
            size++;
            return;
        }
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndexByKey(key)];
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

    private int getIndexByKey(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        size = 0;
        int newCapacity = table.length * DEFAULT_MULTIPLIER;
        Node<K, V>[] currentTable = table;
        table = new Node[newCapacity];

        for (int i = 0; i < currentTable.length; i++) {
            Node<K, V> newNode = currentTable[i];
            while (newNode != null) {
                put(newNode.key, newNode.value);
                newNode = newNode.next;
            }
        }
    }

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
