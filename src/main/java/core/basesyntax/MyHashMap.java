package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float THRESHOLD_COEFFICIENT = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value, null);
        checkSize();
        int index = getIndex(node.key);
        if (table[index] == null) {
            table[index] = node;
            size++;
            return;
        }
        Node<K, V> newNode = table[index];
        while (newNode != null) {
            if (Objects.equals(node.key, newNode.key)) {
                newNode.value = value;
                return;
            }
            if (newNode.next == null) {
                newNode.next = node;
                size++;
                return;
            }
            newNode = newNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void checkSize() {
        if (size > (int) (table.length * THRESHOLD_COEFFICIENT)) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] previousTable = table;
        table = new Node[table.length << 1];
        size = 0;

        for (Node<K, V> node : previousTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K, V> {
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
