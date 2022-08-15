package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INCREASE = 2;
    private int currentCapacity;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        currentCapacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        increaseCapacity();

        Node<K, V> node = new Node<>(key, value, null);
        int index = getBucketIndex(key);
        if (table[index] == null) {
            table[index] = node;
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
                currentNode.next = node;
                size++;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getBucketIndex(key)];
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

    private int getHash(K key) {
        int hash;
        return (key == null) ? 0 : Math.abs((hash = key.hashCode()) ^ (hash >>> 16));
    }

    private int getBucketIndex(K key) {
        if (getHash(key) == 0) {
            return 0;
        }
        return Math.abs(getHash(key) % table.length);
    }

    private void increaseCapacity() {
        if (size == currentCapacity * LOAD_FACTOR) {
            size = 0;
            currentCapacity = currentCapacity * INCREASE;
            Node<K, V>[] oldTable = table;
            table = new Node[currentCapacity];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
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
}
