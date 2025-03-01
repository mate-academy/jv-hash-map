package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_CAPACITY_INCREASE = 1;
    private static final int CONSTANT_FOR_HASH = 0x7FFFFFFF;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int capacity;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        insertNodeInMap(table, new Node<>(key, value));
    }

    @Override
    public V getValue(K key) {
        int indexTable = indexByKey(key);
        if (table[indexTable] != null) {
            Node<K, V> currentNode = table[indexTable];
            do {
                if (Objects.equals(currentNode.key, key)) {
                    return currentNode.value;
                }
                if (currentNode.next == null) {
                    break;
                }
                currentNode = currentNode.next;
            } while (currentNode != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        capacity = capacity << DEFAULT_CAPACITY_INCREASE;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] copyMap = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : copyMap) {
            if (node == null) {
                continue;
            }
            Node<K, V> currentNode = node;
            do {
                Node<K, V> nextNode = currentNode.next;
                insertNodeInMap(table, currentNode);
                if (nextNode == null) {
                    break;
                }
                currentNode = nextNode;
            } while (currentNode != null);
        }
    }

    private void insertNodeInMap(Node<K, V>[] map, Node<K, V> node) {
        int indexTable = indexByKey(node.key);
        if (map[indexTable] != null) {
            Node<K, V> currentNode = map[indexTable];
            do {
                if (Objects.equals(currentNode.key, node.key)) {
                    currentNode.value = node.value;
                    return;
                }
                if (currentNode.next == null) {
                    break;
                }
                currentNode = currentNode.next;
            } while (currentNode != null);
            currentNode.next = node;
        }
        if (map[indexTable] == null) {
            map[indexTable] = node;
        }
        node.next = null;
        size++;
    }

    private int indexByKey(K key) {
        return key == null ? 0 : key.hashCode() & CONSTANT_FOR_HASH % capacity;
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            hash = key == null ? 0 : key.hashCode() & CONSTANT_FOR_HASH;
        }
    }
}
