package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_MULTIPLIER = 2;

    private Node<K, V>[] table;
    private int size;
    private float threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = table.length * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (threshold == size) {
            checkSize();
        }
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        Node<K, V> node = table[index];
        if (Objects.equals(key, node.key)) {
            node.value = value;
            return;
        }
        while (node.next != null) {
            node = node.next;
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
        }
        node.next = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> newNode = table[getIndex(key)];
        while (newNode != null) {
            if (Objects.equals(newNode.key, key)) {
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

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }

    private void checkSize() {
        int newSize = table.length * DEFAULT_MULTIPLIER;
        final Node<K, V>[] oldTable = table;
        table = new Node[newSize];
        threshold = table.length * LOAD_FACTOR;
        size = 0;
        for (Node<K, V> oldNode : oldTable) {
            while (oldNode != null) {
                put(oldNode.key, oldNode.value);
                oldNode = oldNode.next;
            }
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
