package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(key, value);
        putVal(newNode);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = new Node<>();
        currentNode = table[getIndexFromHash(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.nextNode;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putVal(Node<K, V> newNode) {
        if (table[getIndexFromHash(newNode.key)] == null) {
            table[getIndexFromHash(newNode.key)] = newNode;
            size++;
        } else {
            Node<K, V> currentNode = table[getIndexFromHash(newNode.key)];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, newNode.key)) {
                    currentNode.value = newNode.value;
                    return;
                }
                if (currentNode.nextNode == null) {
                    currentNode.nextNode = newNode;
                    size++;
                    return;
                }
                currentNode = currentNode.nextNode;
            }
        }
    }

    private void resize() {
        int threshold = (int) (table.length * LOAD_FACTOR);
        if (size >= threshold) {
            int capacity = table.length * 2;
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[capacity];
            Node<K, V> currentNode = new Node<>();
            for (Node<K, V> kvNode : oldTable) {
                currentNode = kvNode;
                while (currentNode != null) {
                    putVal(new Node<>(currentNode.key, currentNode.value));
                    currentNode = currentNode.nextNode;
                }
            }
        }
    }

    private int getIndexFromHash(K key) {
        return Math.abs(Objects.hashCode(key) % table.length);
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node() {

        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
