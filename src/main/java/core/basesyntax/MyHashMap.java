package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] nodes;
    private int size;
    private double threshold;

    public MyHashMap() {
        nodes = new Node[DEFAULT_CAPACITY];
        threshold = DEFAULT_CAPACITY * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = getIndex(key);
        if (nodes[index] == null) {
            nodes[index] = new Node<>(key, value);
            size++;
            return;
        }
        Node<K, V> current = nodes[index];
        while (current.next != null
                || Objects.equals(current.key, key)) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        current.next = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = nodes[index];
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

    public int getIndex(K key) {
        return key == null ? 0 : (Math.abs(key.hashCode() % nodes.length));
    }

    private void resize() {
        Node<K, V>[] oldNodes = nodes;
        nodes = (Node<K, V>[]) new Node[oldNodes.length * CAPACITY_MULTIPLIER];
        size = 0;
        threshold = nodes.length * CAPACITY_MULTIPLIER;
        for (Node<K, V> node : oldNodes) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
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
