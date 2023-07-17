package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        putValue(hash(key), key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
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

    private int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void putValue(int hash, K key, V value) {
        if (table[hash] == null) {
            table[hash] = new Node<>(key, value, null);
        } else {
            Node<K, V> newNode = table[hash];
            while (newNode.next != null) {
                if (Objects.equals(key, newNode.key)) {
                    break;
                }
                newNode = newNode.next;
            }
            if (Objects.equals(key, newNode.key)) {
                newNode.value = value;
                size--;
            } else {
                newNode.next = new Node<>(key, value, null);
            }
        }
    }

    private void resize() {
        if (size >= threshold) {
            int capacity = table.length;
            capacity = capacity << 1;
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            Node<K, V>[] oldTable = table;
            table = new Node[capacity];
            transfer(oldTable);
        }
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            Node<K, V> newNode = node;
            while (newNode != null) {
                putValue(hash(newNode.key), newNode.key, newNode.value);
                newNode = newNode.next;
            }
        }
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
}
