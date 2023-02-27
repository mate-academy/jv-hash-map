package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int LOAD_FACTOR = 75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkThreshold();
        int bucket = hash(key);
        Node<K, V> node = new Node<>(null, key, value);
        Node<K, V> nextNode = table[bucket];
        if (nextNode == null) {
            table[bucket] = node;
            size++;
            return;
        }
        while (nextNode != null) {
            if (Objects.equals(nextNode.key, node.key)) {
                nextNode.value = value;
                return;
            } else if (nextNode.next == null) {
                break;
            }
            nextNode = nextNode.next;
        }
        nextNode.next = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];
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

    private void checkThreshold() {
        if ((table.length * LOAD_FACTOR) / 100 < size) {
            resize();
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        Node<K, V>[] newTable = table;
        table = new Node[table.length << 1];
        size = 0;
        for (Node<K, V> node : newTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(Node<K, V> next, K key, V value) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}
