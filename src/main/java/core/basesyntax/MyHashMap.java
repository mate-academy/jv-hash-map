package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int INITIAL_CAPACITY = 16;
    static final double LOAD_FACTOR = 0.75;
    static final int GROWING_INDEX = 2;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        if (size == 0) {
            putFirst(key, value);
        } else {
            int bucket = getBucket(getHash(node.key), table.length);
            if (!checkTableLoading()) {
                resize();
            }
            putToBucket(node, table, bucket);
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        Node<K, V> node = table[getBucket(getHash(key), table.length)];
        while (!Objects.equals(node.key, key)) {
            node = node.next;
        }
        return node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void putFirst(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        table[getBucket(getHash(node.key), table.length)] = node;
        size++;
    }

    public void resize() {
        Node<K, V>[] newTable = new Node[table.length * GROWING_INDEX];
        for (Node<K, V> node : table) {
            if (node != null) {
                do {
                    int bucket = getBucket(getHash(node.key), newTable.length);
                    putToBucket(node, newTable, bucket);
                    Node<K, V> oldNode = node;
                    node = node.next;
                    oldNode.next = null;
                } while (node != null);
            }
        }
        table = newTable;
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    public boolean checkTableLoading() {
        return ++size != threshold;
    }

    public boolean isBucketEmpty(int bucket, Node<K, V>[] table) {
        return table[bucket] == null;
    }

    public void putToBucket(Node<K, V> node, Node<K, V>[] table, int bucket) {
        if (isBucketEmpty(bucket, table)) {
            table[bucket] = node;
        } else {
            Node<K, V> existingNode = table[bucket];
            while (existingNode.next != null && (!Objects.equals(existingNode.key, node.key))) {
                existingNode = existingNode.next;
            }
            if (Objects.equals(existingNode.key, node.key)) {
                existingNode.value = node.value;
                size--;
            } else {
                existingNode.next = node;
            }
        }
    }

    public int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    public int getBucket(int hash, int tableLength) {
        return hash % tableLength;
    }
}
