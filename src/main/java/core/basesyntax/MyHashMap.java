package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double loadFactor = 0.75;
    private int size;
    private int capacity;
    private Node<K, V> [] table;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= loadFactor * capacity) {
            table = resize();
        }
        int hashKey = hash(key);
        if (table[hashKey] == null) {
            table[hashKey] = new Node<>(hashKey, key, value, null);
            size++;
            return;
        }
        Node node = table[hashKey];
        while (node.next != null && !Objects.equals(node.key, key)) {
            node = node.next;
        }
        if (Objects.equals(node.key, key)) {
            node.value = value;
            return;
        }
        node.next = new Node<>(hashKey, key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[hash(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return (V) currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        if (h < 0) {
            h = Math.abs(h);
        }
        return h % capacity;
    }

    private Node [] resize() {
        size = 0;
        capacity = capacity * 2;
        Node[] oldTable = table;
        table = new Node[capacity];
        for (int i = 0; i < oldTable.length; i++) {
            Node currentNode = oldTable[i];
            while (currentNode != null) {
                put((K) currentNode.key, (V) currentNode.value);
                currentNode = currentNode.next;
            }
        }
        return table;
    }
}
