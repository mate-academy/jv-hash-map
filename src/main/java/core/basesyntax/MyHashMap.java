package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INNITIAL_CAPACITY = 16;
    private static final float DEFAULT_FACTORY_LOAD = 0.75f;
    private Node<K, V>[] table;
    private int capacity;
    private int maxSize;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INNITIAL_CAPACITY];
        capacity = DEFAULT_INNITIAL_CAPACITY;
        maxSize = (int) (capacity * DEFAULT_FACTORY_LOAD);
    }

    public MyHashMap(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity must be more than 0");
        }
        table = new Node[initialCapacity];
        capacity = initialCapacity;
        maxSize = (int) (capacity * DEFAULT_FACTORY_LOAD);
    }

    @Override
    public void put(K key, V value) {
        if (size == maxSize) {
            resize();
        }
        int index = getIndex(getHash(key), capacity);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = new Node<>(key, value, null);
                    break;
                }
                current = current.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(getHash(key), capacity);
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

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(int hash, int capacity) {
        return hash == 0 ? 0 : hash % capacity;
    }

    private <K> int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[capacity * 2];
        for (Node currentNode : oldTable) {
            while (currentNode != null) {
                put((K) currentNode.key,(V) currentNode.value);
                size--;
                currentNode = currentNode.next;
            }
        }
        capacity = table.length;
        maxSize = (int) (capacity * DEFAULT_FACTORY_LOAD);
    }
}
