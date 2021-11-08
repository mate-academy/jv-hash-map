package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int GROWTH_MULTIPLIER = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int capacity;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resizeAndTransform();
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> node = table[index];
            while (true) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = newNode;
                    size++;
                    return;
                }
                node = node.next;
            }
        }
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

    private void resizeAndTransform() {
        if (size != threshold) {
            return;
        }
        size = 0;
        capacity = table.length * GROWTH_MULTIPLIER;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] tempTable = table;
        table = new Node[capacity];
        for (Node<K, V> currentNode : tempTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private static class Node<T, V> {
        private final T key;
        private V value;
        private Node<T, V> next;

        public Node(T key, V value, Node<T, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
