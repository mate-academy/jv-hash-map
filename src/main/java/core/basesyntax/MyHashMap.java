package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int RESIZE_FACTOR = 2;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = Math.abs(key != null ? key.hashCode() : 0);
        int index = hash % table.length;
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                if (Objects.equals(current.value, value)) {
                    return;
                } else {
                    current.value = value;
                    return;
                }
            }
            if (current.nextNode == null) {
                current.nextNode = new Node<>(hash, key, value, null);
                size++;
                return;
            }
            current = current.nextNode;
        }

        table[index] = new Node<>(hash, key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = Math.abs(key != null ? key.hashCode() : 0);
        int index = hash % table.length;
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.nextNode;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size >= table.length * LOAD_FACTOR) {
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * RESIZE_FACTOR];
            size = 0;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.nextNode;
                }
            }
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(int hash, K key, V value, Node<K, V> nextNode) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
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
            return Objects.equals(key, node.key) && Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
}
