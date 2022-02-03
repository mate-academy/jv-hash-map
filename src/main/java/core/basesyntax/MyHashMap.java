package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int SIZE_INCREASE_INDEX = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            size = 0;
            resize();
        }
        int index = getIndex(key);
        if (table[index] == null) {
            putNodeInEmptyBucket(key, value, index);
        } else {
            putNodeIfCollision(key, value, index);
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> newNode = table[index];
        while (newNode != null) {
            if (Objects.equals(key, newNode.key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putNodeInEmptyBucket(K key, V value, int index) {
        Node<K, V> newNode = new Node<>(key, value, null);
        table[index] = newNode;
        size++;
    }

    private void putNodeIfCollision(K key, V value, int index) {
        Node<K, V> startNode = table[index];
        while (startNode != null) {
            if (Objects.equals(key, startNode.key)) {
                startNode.value = value;
                return;
            }
            startNode = startNode.next;
        }
        Node<K, V> newFirstNode = new Node<K, V>(key, value, table[index]);
        table[index] = newFirstNode;
        size++;
    }

    private void resize() {
        Node<K, V>[] copyTable = table;
        table = new Node[table.length * SIZE_INCREASE_INDEX];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> node: copyTable) {
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

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }
}
