package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private float loadFactor;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0 || loadFactor <= 0) {
            throw new IllegalArgumentException();
        }
        this.table = new Node[initialCapacity];
        this.loadFactor = loadFactor;
        this.threshold = (int) (initialCapacity * loadFactor);
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        int hash = getHash(key);
        int index = getIndex(hash, table.length);

        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && (Objects.equals(key, node.key))) {
                node.value = value;
                return;
            }
        }
        addNode(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        int index = getIndex(hash, table.length);

        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && (Objects.equals(key, node.key))) {
                return node.value;
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addNode(int hash, K key, V value, int index) {
        Node<K, V> node = table[index];
        table[index] = new Node<>(hash, key, value, node);
        size++;

        if (size >= threshold) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;

        int newCapacity = oldCapacity * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        threshold = (int) (newCapacity * loadFactor);

        for (int i = 0; i < oldCapacity; i++) {
            for (Node<K, V> node = oldTable[i]; node != null; ) {
                Node<K, V> next = node.next;

                int index = getIndex(node.hash, newCapacity);
                node.next = newTable[index];
                newTable[index] = node;

                node = next;
            }
        }
        table = newTable;
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(int hash, int length) {
        return hash & (length - 1);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
