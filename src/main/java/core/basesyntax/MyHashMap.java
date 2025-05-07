package core.basesyntax;

import static java.util.Objects.hash;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
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
        Node<K, V> thisNode = table[bucketIndex];
        while (thisNode != null) {
            if (Objects.equals(thisNode.key, key)) {
                return thisNode.value;
            }
            thisNode = thisNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void add(Node<K, V> node, int bucketIndex) {
        table[bucketIndex] = node;
        size++;
    }

    private void putNew(Node<K, V> node, int bucketIndex) {
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

    private int calculateIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        int newCapacity = table.length * 2;
        table = new Node[newCapacity];
        threshold *= 2;
        size = 0;
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] table) {
        for (Node<K, V> each : table) {
            while (each != null) {
                put(each.key, each.value);
                each = each.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;
        private int hash;

        Node(K key, V value, int hash, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }
}
