package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int SIZE_MULTIPLIER = 2;
    private int threshold;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int)(DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> indexNode = table[getIndex(key)];
        Node<K, V> newNode = new Node<>(getHash(key), key, value);
        if (indexNode == null) {
            table[getIndex(key)] = newNode;
        } else {
            Node<K, V> previousNode = null;
            while (indexNode != null) {
                if (Objects.equals(indexNode.key, key)) {
                    indexNode.value = value;
                    return;
                }
                previousNode = indexNode;
                indexNode = indexNode.next;
            }
            previousNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNode(Object key) {
        Node<K, V> node = table[getIndex(key)];
        if (node == null) {
            return null;
        }
        while (!Objects.equals(node.key, key)) {
            node = node.next;
        }
        return node;
    }

    private int getIndex(Object key) {
        return Math.abs(getHash(key) % table.length);
    }

    private int getHash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldInnerArray = table;
        int newNewTableCapacity = (int) (table.length * SIZE_MULTIPLIER);
        table = new Node[newNewTableCapacity];
        threshold = (int) (newNewTableCapacity * LOAD_FACTOR);
        for (Node<K, V> node : oldInnerArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
