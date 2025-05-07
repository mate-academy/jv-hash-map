package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTORY = 0.75f;
    private static final int RESIZE_COEFFICIENT = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];

    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * LOAD_FACTORY) {
            resize();
        }
        int index = (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
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
        int index = (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
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
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * RESIZE_COEFFICIENT];
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                while (oldTable[i] != null) {
                    put(oldTable[i].key, oldTable[i].value);
                    oldTable[i] = oldTable[i].nextNode;
                }
            }
        }
    }
}

