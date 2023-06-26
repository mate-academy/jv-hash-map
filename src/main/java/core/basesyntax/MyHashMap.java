package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap(int capacity) {
        this.table = (Node<K, V>[]) new Node[capacity];
    }

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkThreshold();
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

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> item : oldTable) {
            Node<K, V> node = item;
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void resize() {
        int newSize = 2 * table.length;
        Node<K, V>[] oldTable = table;
        table = new Node[newSize];
        size = 0;
        transfer(oldTable);
    }

    private void checkThreshold() {
        int threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        if (size >= threshold) {
            resize();
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
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
