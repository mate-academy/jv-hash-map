package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K,V> inputNode = new Node<>(key, value, null);
        Node<K,V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = inputNode;
        }
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = inputNode;
                break;
            }
            currentNode = currentNode.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
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

    private void resize() {
        if (size >= threshold) {
            size = 0;
            Node<K,V>[] oldTable = table;
            Node<K,V>[] newTable = new Node[table.length * GROW_FACTOR];
            threshold = (int) (newTable.length * DEFAULT_LOAD_FACTOR);
            table = newTable;
            transfer(oldTable);
        }
    }

    private void transfer(Node<K,V>[] oldTable) {
        for (Node<K,V> oldTableNode : oldTable) {
            while (oldTableNode != null) {
                put(oldTableNode.key, oldTableNode.value);
                oldTableNode = oldTableNode.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
