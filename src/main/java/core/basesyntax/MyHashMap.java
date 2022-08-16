package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int COEFFICIENT_GROW = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] map;
    private int size;

    public MyHashMap() {
        map = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 >= map.length * LOAD_FACTOR) {
            resizeMap();
        }
        putItem(key, value);
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        Node<K, V> node = map[index];
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

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % map.length;
    }

    private void putItem(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getBucketIndex(newNode.key);
        if (map[index] == null) {
            map[index] = newNode;
            size++;
            return;
        }
        Node<K, V> node = map[index];
        Node<K, V> oldNode;
        do {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            oldNode = node;
            node = node.next;
        } while (node != null);

        size++;
        oldNode.next = newNode;
    }

    private void resizeMap() {
        size = 0;
        Node<K, V>[] oldMap = map;
        map = (Node<K, V>[]) new Node[map.length * COEFFICIENT_GROW];

        for (Node<K, V> node : oldMap) {
            while (node != null) {
                putItem(node.key, node.value);
                node = node.next;
            }
        }
    }

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
}
