package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final int NUMBER_OF_SCALING_NEW_TABLE = 2;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_SIZE = 0;
    static final int HASH_CODE_OF_NULL = 0;

    private int threshold;
    private int size;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];

    public MyHashMap() {
        recountThreshold();
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        Node<K, V> node = table[index];
        Node<K, V> prevNode = null;

        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            } else {
                prevNode = node;
                node = node.next;
            }
        }

        Node<K, V> newNode = new Node<>(key, value);

        if (table[index] == null) {
            table[index] = newNode;
        } else {
            prevNode.next = newNode;
        }

        if (++size > threshold) {
            grow();
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
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

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.hash = hash(key);
            this.key = key;
            this.value = value;
        }
    }

    private int hash(Object key) {
        return key == null ? HASH_CODE_OF_NULL : Math.abs(key.hashCode() % table.length);
    }

    private void recountThreshold() {
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    private void grow() {
        final Node<K, V>[] oldTable = table;
        int newCapacity = table.length * NUMBER_OF_SCALING_NEW_TABLE;
        table = new Node[newCapacity];
        recountThreshold();
        size = DEFAULT_SIZE;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
