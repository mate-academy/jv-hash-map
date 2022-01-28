package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int SIZE_INCREASE_INDEX = 2;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldMap = table;
        table = new Node[table.length * SIZE_INCREASE_INDEX];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> node: oldMap) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;

            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void putNodeInEmptyBucket(K key, V value, int index) {
        table[index] = new Node<>(key, value, null);
        size++;
    }

    private void putNodeIfKeyExistsOrCollision(K key, V value, int index) {
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                break;
            }
            node = node.next;
        }
        size++;
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        if (table[index] == null) {
            putNodeInEmptyBucket(key, value, index);
        } else {
            putNodeIfKeyExistsOrCollision(key, value, index);
        }
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node<K, V> newNode = table[hash];
        while (newNode != null) {
            if (Objects.equals(key, newNode.key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
