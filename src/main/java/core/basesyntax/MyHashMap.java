package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;
    private int capacity;

    @Override
    public void put(K key, V value) {
        checkCapacity();
        Node<K, V> node = new Node<>(key, value);
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = node;
        } else {
            Node<K, V> previousNode = table[index];
            while (previousNode != null) {
                if (Objects.equals(key, previousNode.key)) {
                    previousNode.value = value;
                    return;
                }
                if (previousNode.next == null) {
                    previousNode.next = node;
                    break;
                }
                previousNode = previousNode.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (size > 0) {
            Node<K, V> current = table[getIndex(key)];
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    return current.value;
                }
                current = current.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private void checkCapacity() {
        if (capacity == 0) {
            table = new Node[DEFAULT_CAPACITY];
            capacity = DEFAULT_CAPACITY;
        }
        if (size >= capacity * LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        capacity = capacity * CAPACITY_MULTIPLIER;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
