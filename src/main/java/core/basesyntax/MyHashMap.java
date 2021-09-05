package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int CAPACITY_RESIZE_FACTOR = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            changeSize();
        }
        Node<K, V> node = new Node<>(key, value, null);
        int index = hash(key);
        if (table[index] == null) {
            table[index] = node;
            size++;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = node;
                    size++;
                    return;
                } else {
                    currentNode = currentNode.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
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

    private int hash(K key) {
        int hashKey;
        int h;
        return key == null ? 0 : Math.abs(key.hashCode() % DEFAULT_INITIAL_CAPACITY);
    }

    private void changeSize() {
        Node<K, V>[] currentTable = table;
        int currentCapacity = currentTable.length;
        table = new Node[(currentCapacity * CAPACITY_RESIZE_FACTOR)];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        size = 0;
        for (Node<K, V> node : currentTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

}
