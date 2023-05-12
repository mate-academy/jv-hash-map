package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node [DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        putValue(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[findIndex(key)];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int findIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void putValue(K key, V value) {
        if (table[findIndex(key)] == null) {
            table[findIndex(key)] = new Node<>(key, value);
        } else {
            addNewNode(table[findIndex(key)], key, value);
        }
    }

    private void addNewNode(Node<K, V> currentNode, K key, V value) {
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                size--;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value);
                return;
            }
            currentNode = currentNode.next;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * GROW_FACTOR];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                putValue(node.key, node.value);
                node = node.next;
            }
        }
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
