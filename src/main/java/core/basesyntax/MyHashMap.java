package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;

    private int size;
    private Node<K, V>[] table;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int bucket = getBucketIndex(key);
        Node<K, V> currentNode = table[bucket];

        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }

        if (size >= table.length * LOAD_FACTOR) {
            resize();
            bucket = getBucketIndex(key);
        }

        table[bucket] = new Node<>(key, value, table[bucket]);
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucket = getBucketIndex(key);
        Node<K, V> currentNode = table[bucket];

        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getBucketIndex(K key) {
        return Math.abs(getHash(key) % table.length);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * RESIZE_MULTIPLIER];
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> nextNode = node.next;
                put(node.key, node.value);
                node = nextNode;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
