package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOADER_SIZE = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private int size;
    private int threshold;
    private int capacity;
    private Node<K, V> [] table;

    MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOADER_SIZE);
        capacity = DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        int index = countHash(key);
        putNode(key, value, index);
    }

    @Override
    public V getValue(K key) {
        int tableIndex = countHash(key);
        Node<K, V> currentNode = table[tableIndex];
        if (currentNode != null) {
            while (currentNode.next != null) {
                if (Objects.equals(currentNode.key, key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
            return currentNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putNode(K key, V value, int index) {
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> currentNode = table[index];
            do {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    break;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(key, value, null);
                    size++;
                }
                currentNode = currentNode.next;
            } while (true);
        }
    }

    private int countHash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private void resize() {
        size = 0;
        capacity *= 2;
        threshold = (int) (capacity * DEFAULT_LOADER_SIZE);
        final Node<K, V> [] oldTables = table;
        table = new Node[capacity];
        copyFrom(oldTables);
    }

    private void copyFrom(Node<K, V>[] oldTables) {
        for (Node<K, V> currNode : oldTables) {
            while (currNode != null) {
                put(currNode.key, currNode.value);
                currNode = currNode.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
