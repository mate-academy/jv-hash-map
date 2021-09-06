package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int MULTIPLIER_FOR_THRESHOLD_AND_CAPACITY = 2;
    private int threshold;
    private int size;
    private int capacity;
    private Node<K, V>[] nodes;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
        nodes = new Node[capacity];
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        increaseArray();
        Node<K, V> putNode = new Node<>(key, value, null);
        int position = index(key);
        Node<K, V> positionNode = nodes[position];
        if (positionNode == null) {
            nodes[position] = putNode;
            size++;
            return;
        }
        while (positionNode != null) {
            if (Objects.equals(key, positionNode.key)) {
                positionNode.value = value;
                return;
            }
            if (positionNode.next == null) {
                positionNode.next = putNode;
                size++;
                return;
            }
            positionNode = positionNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> getNode = nodes[index(key)];
        while (getNode != null) {
            if (Objects.equals(key, getNode.key)) {
                return getNode.value;
            }
            getNode = getNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int index(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private void increaseArray() {
        if (size == threshold) {
            size = 0;
            threshold *= MULTIPLIER_FOR_THRESHOLD_AND_CAPACITY;
            capacity *= MULTIPLIER_FOR_THRESHOLD_AND_CAPACITY;
            Node<K, V>[] tempArray = nodes;
            nodes = new Node[capacity];
            for (Node<K, V> node : tempArray) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
