package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int SIZE_MULTIPLIER = 2;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= (int) (table.length * LOAD_FACTOR)) {
            resize();
        }
        Node<K, V> indexNode = table[getIndex(key)];
        Node<K, V> newNode = new Node<>(key, value);
        if (indexNode == null) {
            table[getIndex(key)] = newNode;
        } else {
            getLastNode(key, value, indexNode, newNode);
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

    private Node<K, V> getNode(K key) {
        Node<K, V> node = table[getIndex(key)];
        if (node == null) {
            return null;
        }
        while (!Objects.equals(node.key, key)) {
            node = node.next;
        }
        return node;
    }

    private void getLastNode(K key, V value, Node<K, V> indexNode, Node<K, V> newNode) {
        Node<K, V> previousNode = null;
        while (indexNode != null) {
            if (Objects.equals(indexNode.key, key)) {
                indexNode.value = value;
                size--;
                return;
            }
            previousNode = indexNode;
            indexNode = indexNode.next;
        }
        previousNode.next = newNode;
    }

    private int getIndex(K key) {
        return Math.abs(getHash(key) % table.length);
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldInnerArray = table;
        int newNewTableCapacity = (int) (table.length * SIZE_MULTIPLIER);
        table = new Node[newNewTableCapacity];
        transfer(oldInnerArray);
    }

    private void transfer(Node<K, V>[] oldInnerArray) {
        for (Node<K, V> node : oldInnerArray) {
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

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
