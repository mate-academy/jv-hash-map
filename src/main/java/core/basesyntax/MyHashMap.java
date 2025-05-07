package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K,V> implements MyMap<K,V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K,V> node = table[getBucketIndex(hash(key))];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
        table[getBucketIndex(hash(key))] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = table[getBucketIndex(hash(key))];
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

    private void resize() {
        if (size > (threshold)) {
            size = 0;
            Node<K, V>[] temp = table;
            table = (Node<K, V>[]) new Node[table.length << 1];
            threshold = (int) (table.length * LOAD_FACTOR);
            for (Node<K, V> node : temp) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getBucketIndex(int hash) {
        return Math.abs(hash) % table.length;
    }

    private static class Node<K,V> {
        private V value;
        private K key;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
