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
        int index = getBucketIndex(key);
        putNode(key, value, index);
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int tableIndex = getBucketIndex(key);
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
                    return;
                }
                currentNode = currentNode.next;
            } while (true);
        }
    }

    private int getBucketIndex(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private void resize() {
        size = 0;
        capacity *= 2;
        threshold = (int) (capacity * DEFAULT_LOADER_SIZE);
        final Node<K, V> [] oldTable = table;
        table = new Node[capacity];
        copyFrom(oldTable);
    }

    private void copyFrom(Node<K, V>[] oldTable) {
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
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
