package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final short RESIZE_FACTOR = 2;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resizeMap();
        }
        int index = getIndexByKey(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        }
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value);
                size++;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndexByKey(key)];
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

    private void resizeMap() {
        size = 0;
        Node<K, V>[] nodeArray = table;
        table = new Node[nodeArray.length * RESIZE_FACTOR];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (Node<K, V> node : nodeArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndexByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
