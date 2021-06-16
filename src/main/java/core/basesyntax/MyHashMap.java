package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private int capacity = DEFAULT_CAPACITY;
    private static final float LOAD_FACTORY = 0.75f;
    int size;

    private Node[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == capacity * LOAD_FACTORY) {
            resize();
        }
        int index = (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
        Node<K, V> newNode = new Node<>(key, value, null);

        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.nextNode == null) {
                    currentNode.nextNode = newNode;
                    break;
                }
                currentNode = currentNode.nextNode;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.nextNode;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        Node(K key, V value, Node<K, V> nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node[] oldTable = table;
        table = new Node[newCapacity]; // size 32
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                while (oldTable[i] != null) {
                    put((K) oldTable[i].key, (V) oldTable[i].value);
                    oldTable[i] = oldTable[i].nextNode;
                }
            }
        }
    }
}

