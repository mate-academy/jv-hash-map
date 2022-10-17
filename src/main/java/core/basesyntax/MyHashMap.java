package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INCREASE_FACTOR = 2;
    private Node<K, V>[] table;
    private float threshold;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        threshold = INITIAL_CAPACITY * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            increaseCapacity();
        }
        Node<K, V> node = new Node<>(key, value, null);
        putInTable(node, table);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (table[index] == null) {
            return null;
        }
        Node<K, V> currentNode = table[index];
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

    private void increaseCapacity() {
        size = 0;
        Node<K, V>[] oldTable = table;
        int newCapacity = table.length * INCREASE_FACTOR;
        table = (Node<K, V>[]) new Node[newCapacity];
        transfer(oldTable, table);
        threshold = table.length * LOAD_FACTOR;
    }

    private void putInTable(Node<K, V> node, Node<K, V>[] table) {
        int index = getIndex(node.key);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = node;
            size++;
            return;
        }
        while (true) {
            if (Objects.equals(node.key, currentNode.key)) {
                currentNode.value = node.value;
                return;
            } else if (currentNode.next == null) {
                currentNode.next = node;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void transfer(Node<K, V>[] table, Node<K,V>[] newTable) {
        for (Node<K, V> node : table) {
            if (node == null) {
                continue;
            }
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                Node<K, V> oldNext = currentNode.next;
                currentNode.next = null;
                putInTable(currentNode, newTable);
                currentNode = oldNext;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
