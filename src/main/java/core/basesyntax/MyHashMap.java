package core.basesyntax;

import java.util.Arrays;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOADING_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int size = 0;
    private int capacity = 16;

    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    public int getIndex(K key) {
        return Math.abs(Objects.hash(key) % capacity);
    }

    public void grow() {
        Node<K, V> [] tableCopy = Arrays.copyOf(table, capacity);
        table = new Node[capacity * 2];
        int oldCapacity = capacity;
        capacity *= 2;
        size = 0;
        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> currentNode = tableCopy[i];
            if (currentNode != null) {
                put(currentNode.key, currentNode.value);
                if (currentNode.next != null) {
                    while (currentNode.next != null) {
                        put(currentNode.next.key, currentNode.next.value);
                        currentNode = currentNode.next;
                    }
                }
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == capacity * LOADING_FACTOR) {
            grow();
            put(key, value);
            return;
        }

        if (key == null) {
            Node<K, V> currentNode = table[0];
            if (table[0] == null) {
                table[0] = new Node<>(key, value);
            } else {
                currentNode.value = value;
                return;
            }
        } else {
            int index = getIndex(key);
            Node<K, V> currentNode = table[index];
            if (table[index] == null) {
                table[index] = new Node<>(key, value);
            } else if (table[index] != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                } else {
                    while (currentNode.next != null) {
                        if (Objects.equals(currentNode.key, key)) {
                            currentNode.value = value;
                            return;
                        }
                        currentNode = currentNode.next;
                    }
                    currentNode.next = new Node<>(key, value);
                }
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return table[0].value;
        } else {
            int index = getIndex(key);
            Node<K, V> currentNode = table[index];
            if (currentNode == null) {
                return null;
            } else if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            } else {
                while (currentNode != null) {
                    if (Objects.equals(currentNode.key, key)) {
                        return currentNode.value;
                    }
                    currentNode = currentNode.next;
                }
            }
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
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
