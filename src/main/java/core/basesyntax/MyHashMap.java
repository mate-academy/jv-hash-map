package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_BY = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkForResize();
        int index = getBucketNumber(key);
        Node<K, V> currentNode = table[index];

        if (currentNode == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        }
        while (true) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                break;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value);
                size++;
                break;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getBucketNumber(key);
        Node<K, V> node = table[index];
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

    private void checkForResize() {
        int threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        if (size >= threshold) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * GROW_BY];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getBucketNumber(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
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
