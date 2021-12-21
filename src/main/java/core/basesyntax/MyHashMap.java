package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = calculateIndex(key);
        Node<K, V> firstNode = table[index];
        Node<K, V> newNode = new Node<>(key, value);
        if (firstNode == null) {
            table[index] = newNode;
            size++;
            resizeIfNeeded();
            return;
        }
        addOrReplace(firstNode, newNode);
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K, V> firstNode = table[index];
        for (Node<K, V> node = firstNode; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addOrReplace(Node<K, V> firstNode, Node<K, V> newNode) {
        Node<K, V> node = new Node<>(null, null);
        node.next = firstNode;
        do {
            node = node.next;
            if (Objects.equals(node.key, newNode.key)) {
                node.value = newNode.value;
                return;
            }
        } while (node.next != null);
        node.next = newNode;
        size++;
        resizeIfNeeded();
    }

    private void resizeIfNeeded() {
        if (size > getThreshold()) {
            grow();
        }
    }

    private void grow() {
        int capacity = table.length << 1;
        Node<K, V>[] oldTable = table;
        size = 0;
        table = new Node[capacity];
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int calculateIndex(K key) {
        int hash = key == null ? 0 : key.hashCode();
        return hash & (table.length - 1);
    }

    private float getThreshold() {
        return table.length * DEFAULT_LOAD_FACTOR;
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
