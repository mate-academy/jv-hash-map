package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private static final int RESIZE_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * LOAD_FACTOR) {
            growIfNeeded();
        }
        int index = getIndexByHash(key);
        Node<K, V> node = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = node;
            size++;
        } else {
            putIfCollision(table[index], node, value);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndexByHash(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void growIfNeeded() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * RESIZE_FACTOR];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndexByHash(K key) {
        if (key == null) {
            return 0;
        }
        int hashCode = key.hashCode();
        return Math.abs(key.hashCode()) % table.length;
    }

    private void putIfCollision(Node<K, V> existingNode, Node<K, V> newNode, V value) {
        while (existingNode != null) {
            if (Objects.equals(newNode.key, existingNode.key)) {
                existingNode.value = value;
                return;
            }
            if (existingNode.next == null) {
                existingNode.next = newNode;
                size++;
                return;
            }
            existingNode = existingNode.next;
        }
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
