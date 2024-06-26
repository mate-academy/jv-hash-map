package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        this.table = createTable(DEFAULT_INITIAL_CAPACITY);
        this.size = 0;
    }

    public MyHashMap(int initialCapacity) {
        this.table = createTable(initialCapacity);
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = getBucketIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
        }
        addNode(key, value, index);
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addNode(K key, V value, int index) {
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = createTable(newCapacity);
        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = getBucketIndexForNode(node.key, newCapacity);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
    }

    private int getBucketIndex(K key) {
        int hash = hash(key);
        return indexFor(hash, table.length);
    }

    private int getBucketIndexForNode(K key, int capacity) {
        int hash = hash(key);
        return indexFor(hash, capacity);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private int hash(K key) {
        int h = (key != null) ? key.hashCode() : 0;
        return h ^ (h >>> 16);
    }

    @SuppressWarnings("unchecked")
    private Node<K, V>[] createTable(int size) {
        return (Node<K, V>[]) new Node[size];
    }
}
