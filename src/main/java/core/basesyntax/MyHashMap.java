package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int capacity;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = INITIAL_CAPACITY;
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(key, value);
        putVal(newNode);
    }

    private void putVal(Node<K, V> newNode) {
        if (table[Math.abs(newNode.hashCode % capacity)] == null) {
            table[Math.abs(newNode.hashCode % capacity)] = newNode;
            size++;
        } else {
            Node<K, V> currentNode = table[Math.abs(newNode.hashCode % capacity)];
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

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = new Node<>();
        for (int i = 0; i < capacity; i++) {
            currentNode = table[i];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.nextNode;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int threshold = (int) (capacity * LOAD_FACTOR);
        if (size >= threshold) {
            capacity = capacity * 2;
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

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;
        private int hashCode;

        public Node() {

        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            hashCode = Objects.hashCode(key);
        }
    }
}
