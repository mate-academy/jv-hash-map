package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private final float loadFactor;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int threshold, float loadFactor) {
        this.threshold = threshold;
        this.loadFactor = loadFactor;
        this.table = new Node[threshold];
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndexKey(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
        if (size > threshold * loadFactor) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndexKey(key);
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
    public void remove(K key) {
        int index = getIndexKey(key);
        Node<K, V> node = table[index];
        Node<K, V> prev = null;
        while (node != null) {
            if (node.key.equals(key)) {
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return;
            }
            prev = node;
            node = node.next;
        }
    }

    @Override
    public void resize() {
        int newCapacity = table.length * 2;
        threshold = (int) (DEFAULT_LOAD_FACTOR * newCapacity);
        size = 0;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                put(node.key, node.value);
                Node<K, V> current = node.next;
                while (current != null) {
                    put(current.key, current.value);
                    current = current.next;
                }
            }
        }
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

    private int getIndexKey(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }
}

