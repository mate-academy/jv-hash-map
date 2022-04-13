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
        int hash = hash(key);
        Node<K, V> current = new Node<>(hash, key, value);
        if (nodes[hash] == null) {
            nodes[hash] = current;
            size++;
            return;
        }
        Node node = nodes[hash];
        while (nodes[hash].next != null) {
            if (nodes[hash].getKey() == null && nodes[hash].getKey() == key
                    || nodes[hash].getKey() != null && nodes[hash].getKey().equals(key)) {
                nodes[hash].value = current.value;
                nodes[hash] = node;
                return;
            }
            nodes[hash] = nodes[hash].next;
        }
        if (nodes[hash].getKey() == null && nodes[hash].getKey() == key
                || nodes[hash].getKey() != null && nodes[hash].getKey().equals(key)) {
            nodes[hash].value = current.value;
            nodes[hash] = node;
            return;
        }
        nodes[hash].next = current;
        nodes[hash] = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        if (nodes[hash] == null) {
            return null;
        }
        if (nodes[hash].next == null) {
            if (nodes[hash].getKey() == null && nodes[hash].getKey() == key
                    || nodes[hash].getKey().equals(key)) {
                return nodes[hash].getValue();
            }
        }
        Node node = nodes[hash];
        V result;
        while (nodes[hash].next != null) {
            if (nodes[hash].getKey() == null && nodes[hash].getKey() == key
                    || nodes[hash].getKey() != null && nodes[hash].getKey().equals(key)) {
                result = nodes[hash].getValue();
                nodes[hash] = node;
                return result;
            }
            nodes[hash] = nodes[hash].next;
        }
        if (nodes[hash].getKey() == null && nodes[hash].getKey() == key
                || nodes[hash].getKey() != null && nodes[hash].getKey().equals(key)) {
            result = nodes[hash].getValue();
            nodes[hash] = node;
            return result;
        }
        throw new RuntimeException("Is not exists");
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "MyHashMap{"
                + "nodes=" + Arrays.toString(nodes)
                + '}';
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

        @Override
        public String toString() {
            return "Node{" + "value=" + value
                    + ", key=" + key
                    + ", hash=" + hash
                    + ", next=" + next + '}';
        }

        public V getValue() {
            return value;
        }

        public K getKey() {
            return key;
        }

        public int getHash() {
            return hash;
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

        @Override
        public int hashCode() {
            return Objects.hash(value, key);
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
                put(node.getKey(), node.getValue());
            }
        }
    }
}
