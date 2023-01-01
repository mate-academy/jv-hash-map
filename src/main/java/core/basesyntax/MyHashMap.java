package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        checkCapacity(size);
        putValue(key, value, index);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
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

    private int hash(K key) {
        return Math.abs(Objects.hashCode(key));
    }

    private int getIndex(K key) {
        return hash(key) % table.length;
    }

    private void swapValues(Node<K, V>[] oldTable, Node<K, V>[] table) {
        size = 0;
        for (Node<K, V> currentNode: oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private void putValue(K key, V value, int index) {
        Node<K, V> newNode = new Node<>(key, value, hash(key));
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        Node<K, V> node = table[index];
        for ( ;node.next != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
        }
        if (Objects.equals(node.key, key)) {
            node.value = value;
            return;
        }
        node.next = newNode;
        size++;
    }

    private void checkCapacity(int size) {
        if (size + 1 > Math.abs(table.length * DEFAULT_LOAD_FACTOR)) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        swapValues(oldTable, table);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, int hash) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
