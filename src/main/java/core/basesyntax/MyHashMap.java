package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_COEFFICIENT = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int indexBucket = getBucketIndex(key);
        Node<K, V> checkNode = table[indexBucket];

        while (checkNode != null) {
            if (Objects.equals(checkNode.key, key)) {
                checkNode.value = value;
                return;
            }
            if (checkNode.next == null) {
                checkNode.next = new Node<>(key, value, null);
                size++;
                return;
            }
            checkNode = checkNode.next;
        }
        table[indexBucket] = new Node<>(key, value, null);
        size++;

    }

    @Override
    public V getValue(K key) {
        int indexBucket = getBucketIndex(key);
        Node<K, V> node = table[indexBucket];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final String toString() {
            return key + "=" + value;
        }

    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        size = 0;
        threshold *= GROW_COEFFICIENT;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * GROW_COEFFICIENT];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
