package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int SIZE_MULTIPLICATOR = 2;

    private int size;
    private Node<K, V>[] table;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = returnNodeByKey(key);
        if (node != null) {
            node.value = value;
            return;
        }
        if (size + 1 > threshold) {
            resize();
        }
        node = new Node<>(key, value);
        placeNodeToTable(node);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = returnNodeByKey(key);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = table.length * SIZE_MULTIPLICATOR;
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    private void placeNodeToTable(Node<K, V> node) {
        int index = Math.abs(getHash(node.key) % table.length);
        if (table[index] == null) {
            table[index] = node;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = node;
        }
        size++;
    }

    private Node<K, V> returnNodeByKey(K key) {
        int index = Math.abs(getHash(key) % table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}

