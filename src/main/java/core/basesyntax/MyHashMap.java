package core.basesyntax;

import static java.util.Objects.hash;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V> table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (LOAD_FACTOR * INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, hash(key), null);
        int bucketIndex = calculateIndex(newNode.key);
        if (table[bucketIndex] == null) {
            add(newNode, bucketIndex);
        } else {
            putNew(newNode, bucketIndex);
        }
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = calculateIndex(key);
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

    public void add(Node<K, V> node, int bucketIndex) {
        table[bucketIndex] = node;
        size++;
    }

    public void putNew(Node<K, V> node, int bucketIndex) {
        Node<K, V> currentNode = table[bucketIndex];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, node.key)) {
                currentNode.value = node.value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = node;
                size++;
            }
            currentNode = currentNode.next;
        }
    }

    public int calculateIndex(K key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(key.hashCode() % table.length);
        }
    }

    public void resize() {
        final Node<K, V> oldTable = table;
        int length = table.length;
        int currentCapacity = 2 * length;
        table = new Node[currentCapacity];
        threshold *= 2;
        size = 0;
        transfer(oldTable);
    }

    public void transfer(Node<K, V>[] table) {
        for (Node<K, V> node : table) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        Node(K key, V value, int hash, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }
}
