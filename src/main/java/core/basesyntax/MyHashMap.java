package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLY_FACTOR = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int bucketIndex = (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
        Node<K, V> node = table[bucketIndex];
        if (node == null) {
            table[bucketIndex] = new Node<>(key, value, null);
            size++;
            return;
        }
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
    }

    private void resize() {
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[table.length * CAPACITY_MULTIPLY_FACTOR];
        Node<K, V>[] oldTable = table;
        table = newTable;
        size = 0;
        transfer(oldTable);
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    private void transfer(Node<K, V>[] src) {
        for (Node<K, V> node : src) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
        Node<K, V> currentNode = table[bucketIndex];
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
}
