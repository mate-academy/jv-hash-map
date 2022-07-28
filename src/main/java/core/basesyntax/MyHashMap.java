package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (LOAD_FACTOR * RESIZE_MULTIPLIER);
    }

    @Override
    public void put(K key, V value) {

        if (size == threshold) {
            resize();
        }
        int bucket = hash(key);
        if (table[bucket] == null) {
            table[bucket] = new Node(key, value, null);
            size++;
        } else {
            Node<K, V> node = table[bucket];
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node(key, value, null);
                    size++;
                    return;
                }
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int bucket = hash(key);
        Node<K, V> node = table[bucket];
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

    public void resize() {
        threshold = (int) (table.length * RESIZE_MULTIPLIER * LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * RESIZE_MULTIPLIER];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
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
