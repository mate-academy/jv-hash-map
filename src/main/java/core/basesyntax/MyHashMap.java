package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = calculateIndex(key);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            do {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    break;
                } else if (currentNode.next == null) {
                    currentNode.next = new Node<>(key, value, null);
                    size++;
                    break;
                }
                currentNode = currentNode.next;
            } while (currentNode != null);
        }
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            return null;
        }
        do {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        } while (currentNode != null);

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        int newCapacity = table.length * 2;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private int calculateIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    static class Node<K, V> {
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
