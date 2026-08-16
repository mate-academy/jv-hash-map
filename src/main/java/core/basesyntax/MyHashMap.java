package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = getHashCode(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> currentNode = table[index];
            while (true) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    break;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getHashCode(key);
        Node<K, V> currentNode = table[index];
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

    private int getHashCode(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length << 1;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> currentNode : table) {
            while (currentNode != null) {
                Node<K, V> nextNode = currentNode.next;
                int newIndex = (currentNode.key == null) ? 0
                        : Math.abs(currentNode.key.hashCode() % newCapacity);

                currentNode.next = newTable[newIndex];
                newTable[newIndex] = currentNode;
                currentNode = nextNode;
            }
        }
        table = newTable;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
