package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private double threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K,V> currentNode = table[getIndex(key)];
        if (currentNode == null) {
            table[getIndex(key)] = new Node(key, value);
            size++;
            return;
        }
        while (currentNode.next != null || Objects.equals(currentNode.key, key)) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        currentNode.next = new Node(key, value);
        size++;

    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        Node<K, V> node = table[getIndex(key)];
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        threshold = (int) table.length * DEFAULT_LOAD_FACTOR;
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
