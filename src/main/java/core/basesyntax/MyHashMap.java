package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        increaseCapacity();
        int index = getIndex(key);
        Node<K, V> node = table[index];
        Node<K, V> newNode = new Node<>(key, value);
        if (node == null) {
            table[index] = newNode;
            size++;
        } else {
            putOrSet(index, newNode);
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null && !Objects.equals(node.key, key)) {
            node = node.next;
        }
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putOrSet(int index, Node<K, V> newNode) {
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, newNode.key)) {
                node.value = newNode.value;
                return;
            }
            node = node.next;
        }
        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }

    private int getIndex(K key) {
        return hash(key) % table.length;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void increaseCapacity() {
        if (size > threshold) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = table.length * CAPACITY_MULTIPLIER;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
