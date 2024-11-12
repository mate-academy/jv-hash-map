package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = .75f;
    private static final int SCALE_FACTOR = 2;
    private int size = 0;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int threshold;
    private Node<K, V> [] table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        doubleSizeIfFilled();
        Node<K, V> newNode = new Node<>(key, value);
        int index = calculateIndex(newNode.key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        Node<K, V> currentNode = table[index];
        Node<K, V> previousNode = currentNode;
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            } else {
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
        }
        previousNode.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
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

    private void doubleSizeIfFilled() {
        if (size == capacity * DEFAULT_LOAD_FACTOR) {
            capacity *= SCALE_FACTOR;
            size = 0;
            Node<K, V>[] oldTable = table;
            table = (Node<K, V>[])new Node[capacity];
            for (Node<K, V> oldTableNode : oldTable) {
                if (oldTableNode != null) {
                    put(oldTableNode.key, oldTableNode.value);
                    if (oldTableNode.next != null) {
                        Node<K, V> nodeToTransfer = oldTableNode;
                        while (nodeToTransfer.next != null) {
                            put(nodeToTransfer.next.key, nodeToTransfer.next.value);
                            nodeToTransfer = nodeToTransfer.next;
                        }
                    }
                }
            }
        }
    }

    private int calculateIndex(Object object) {
        return object == null ? 0 : Math.abs(object.hashCode()) % capacity;
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
