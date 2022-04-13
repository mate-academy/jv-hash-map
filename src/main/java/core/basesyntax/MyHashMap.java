package core.basesyntax;

import java.util.Arrays;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_NODES_LENGTH = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private int loaded = (int) (DEFAULT_NODES_LENGTH * LOAD_FACTOR);
    private int size;
    private Node<K, V>[] nodes;

    public MyHashMap() {
        nodes = new Node[DEFAULT_NODES_LENGTH];
    }

    @Override
    public void put(K key, V value) {
        if (size >= loaded) {
            resize();
        }
        int index = hash(key);
        Node<K, V> currentNode = new Node<>(index, key, value);
        if (nodes[index] == null) {
            nodes[index] = currentNode;
            size++;
            return;
        }
        Node current = nodes[index];
        while (current.next != null) {
            if (current.getKey() == null && current.getKey() == key
                    || current.getKey() != null && current.getKey().equals(key)) {
                current.value = currentNode.value;
                return;
            }
            current = current.next;
        }
        if (current.getKey() == null && current.getKey() == key
                || current.getKey() != null && current.getKey().equals(key)) {
            current.value = currentNode.value;
            return;
        }
        current.next = currentNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node current = nodes[index];
        if (current == null) {
            return null;
        }
        if (current.next == null) {
            if (current.getKey() == null && current.getKey() == key
                    || current.getKey().equals(key)) {
                return (V) current.getValue();
            }
        }
        while (current.next != null) {
            if (current.getKey() == null && current.getKey() == key
                    || current.getKey() != null && current.getKey().equals(key)) {
                return (V) current.getValue();

            }
            current = current.next;
        }
        if (current.getKey() == null && current.getKey() == key
                || current.getKey() != null && current.getKey().equals(key)) {
            return (V) current.getValue();
        }
        throw new RuntimeException("Is not exists");
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private V value;
        private K key;
        private int hash;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }

        public V getValue() {
            return value;
        }

        public K getKey() {
            return key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return hash == node.hash && Objects.equals(value, node.value)
                    && Objects.equals(key, node.key);
        }
    }

    private final int hash(Object key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % nodes.length;
    }

    private void resize() {
        nodes = Arrays.copyOf(nodes, nodes.length * 2);
        loaded *= 2;
        transfer();
    }

    private void transfer() {
        Node<K, V>[] nodesCopy = nodes;
        nodes = new Node[nodesCopy.length];
        size = 0;
        for (Node<K, V> node : nodesCopy) {
            if (node == null) {
                continue;
            }
            if (node != null && node.next == null) {
                put(node.getKey(), node.getValue());
            }
            if (node.next != null) {
                while (node.next != null) {
                    put(node.getKey(), node.getValue());
                    node = node.next;
                }
                put
                        (node.getKey(), node.getValue());
            }
        }
    }
}
