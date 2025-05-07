package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MULTIPLIER = 2;

    private Node<K, V>[] table;
    private int threshold;
    private int size;

    static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        addNode(newNode);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void addNode(Node<K, V> currentNode) {
        int index = getIndex(currentNode.key);
        Node<K, V> lastNode = table[index];
        while (lastNode.next != null || Objects.equals(currentNode.key, lastNode.key)) {
            if (Objects.equals(currentNode.key, lastNode.key)) {
                lastNode.value = currentNode.value;
                return;
            }
            lastNode = lastNode.next;
        }
        lastNode.next = currentNode;
        size++;
    }

    private void resize() {
        size = 0;
        int newCapacity = table.length * MULTIPLIER;
        threshold *= MULTIPLIER;
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}



