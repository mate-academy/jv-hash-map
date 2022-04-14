package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_NODES_LENGTH = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private int threshold;
    private int size;
    private Node<K, V>[] nodes;

    public MyHashMap() {
        nodes = new Node[DEFAULT_NODES_LENGTH];
        threshold = (int) (DEFAULT_NODES_LENGTH * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
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
        while (current != null) {
            if (current.key == key || current.key != null && current.key.equals(key)) {
                current.value = currentNode.value;
                return;
            }
            if (current.next == null) {
                current.next = currentNode;
                size++;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node current = nodes[hash(key)];
        while (current != null) {
            if (current.key == key || current.key != null && current.key.equals(key)) {
                return (V) current.value;
            }
            current = current.next;
        }
        return null;
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
        return key == null ? 0 : Math.abs(key.hashCode()) % nodes.length;
    }

    private void resize() {
        final Node<K, V>[] nodesCopy = nodes;
        threshold *= 2;
        nodes = new Node[nodes.length * 2];
        size = 0;
        for (Node<K, V> node : nodesCopy) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
