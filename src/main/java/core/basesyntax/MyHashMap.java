package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int MULTIPLIER = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> nodeNew = table[index];
        while (nodeNew != null) {
            if (Objects.equals(key, nodeNew.key)) {
                return nodeNew.value;
            }
            nodeNew = nodeNew.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void transfer() {
        int newSize = MULTIPLIER * table.length;
        Node<K, V>[] oldTable = table;
        table = new Node[newSize];
        size = 0;
        for (Node<K, V> item : oldTable) {
            Node<K, V> node = item;
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void resize() {
        if (size >= table.length * LOAD_FACTOR) {
            transfer();
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
