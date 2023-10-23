package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int INCREASE_COEFFICIENT = 2;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int threshold;
    private Node<K, V>[] nodes;

    public MyHashMap() {
        threshold = (int) (INCREASE_COEFFICIENT * LOAD_FACTOR);
        nodes = new Node[INITIAL_CAPACITY];
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        Node<K, V> newNode = new Node<>(key, value, null);
        int hashPosition = getNodeIndex(key);
        Node<K, V> positionNode = nodes[hashPosition];
        if (positionNode == null) {
            nodes[hashPosition] = newNode;
            size++;
            return;
        }
        while (positionNode != null) {
            if (Objects.equals(key, positionNode.key)) {
                positionNode.value = value;
                return;
            }
            if (positionNode.next == null) {
                positionNode.next = newNode;
                size++;
                return;
            }
            positionNode = positionNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = nodes[getNodeIndex(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getNodeIndex(K key) {
        return Math.abs(hash(key)) % nodes.length;
    }

    private void checkSize() {
        if (size == threshold) {
            resize();
        }
    }

    private void resize() {
        size = 0;
        threshold *= nodes.length * INCREASE_COEFFICIENT;
        Node<K, V>[] oldArray = nodes;
        nodes = new Node[oldArray.length * INCREASE_COEFFICIENT];
        for (Node<K, V> node : oldArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
