package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_INCREMENT_VALUE_OF_ARRAY = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(newNode);
        if (table[index] == null) {
            add(newNode, index);
        } else {
            attachNode(newNode, index);
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : table) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(Node<K, V> node) {
        return node.key == null ? 0 : Math.abs(node.key.hashCode()) % table.length;
    }

    private void add(Node<K, V> newNode, int index) {
        table[index] = newNode;
        size++;
    }

    private void attachNode(Node<K, V> newNode, int index) {
        Node<K,V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, newNode.key)) {
                currentNode.value = newNode.value;
                return;
            } else if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * DEFAULT_INCREMENT_VALUE_OF_ARRAY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        size = 0;
        transferOfNodes(oldTable);
    }

    private void transferOfNodes(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}


