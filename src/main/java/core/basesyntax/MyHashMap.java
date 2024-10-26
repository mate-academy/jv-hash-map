package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int CAPACITY_MULTIPLIER = 2;

    private int capacity;
    private int threshold;
    private Node<K, V>[] storage;
    private int size;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        storage = new Node[capacity];
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            hash = Math.abs(key == null ? 0 : key.hashCode());
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = findNode(key);
        if (node != null) {
            node.value = value;
        } else {
            if (++size == threshold) {
                resize();
            }
            putNewNodeToStorage(storage, new Node<>(key, value));
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = findNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        capacity *= CAPACITY_MULTIPLIER;
        threshold *= CAPACITY_MULTIPLIER;
        transfer();
    }

    private void transfer() {
        Node<K, V>[] newStorage = new Node[capacity];
        for (Node<K, V> node : storage) {
            while (node != null) {
                Node<K, V> prev = node;
                node = node.next;
                prev.next = null;
                putNewNodeToStorage(newStorage, prev);
            }
        }
        storage = newStorage;
    }

    private void putNewNodeToStorage(Node<K, V>[] storageForNode, Node<K, V> node) {
        int bucket = node.hash % capacity;
        if (storageForNode[bucket] == null) {
            storageForNode[bucket] = node;
        } else {
            Node<K, V> iterateNode = storageForNode[bucket];
            while (iterateNode.next != null) {
                iterateNode = iterateNode.next;
            }
            iterateNode.next = node;
        }
    }

    private Node<K, V> findNode(K key) {
        int hash = Math.abs(key == null ? 0 : key.hashCode());
        int bucket = hash % capacity;
        Node<K, V> node = storage[bucket];
        while (node != null && !Objects.equals(key, node.key)) {
            node = node.next;
        }
        return node;
    }
}
