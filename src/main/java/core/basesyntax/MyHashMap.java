package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int START_ARRAY_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[START_ARRAY_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> nodeToAdd = new Node<>(key, value, table[index]);
        table[index] = nodeToAdd;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> nodeToGet = table[getIndex(key)];
        while (nodeToGet != null) {
            if (checkIfKeyExists(nodeToGet, key)) {
                return nodeToGet.value;
            }
            nodeToGet = nodeToGet.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean checkIfKeyExists(Node<K, V> nodeToCheck, K key) {
        if (nodeToCheck.key == key
                || nodeToCheck.key != null && nodeToCheck.key.equals(key)) {
            return true;
        }
        return false;
    }

    private int getIndex(K key) {
        int hash = key == null ? 0 : key.hashCode();
        return Math.abs(hash) % table.length;
    }

    private void resize() {
        double threshold = table.length * LOAD_FACTOR;
        if (size >= threshold) {
            size = 0;
            Node<K, V>[] newTable = table;
            table = new Node[table.length * 2];
            threshold = table.length * LOAD_FACTOR;
            for (Node<K, V> current : newTable) {
                while (current != null) {
                    put(current.key, current.value);
                    current = current.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
