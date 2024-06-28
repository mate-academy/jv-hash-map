package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
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
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = getBucketIndex(key, table.length);
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
        int index = getBucketIndex(key, table.length);
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
    }

    private void resize() {
        int newCapacity = table.length * GROW_FACTOR;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = getBucketIndex(node.key, newCapacity);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
    }

    private int getBucketIndex(K key, int length) {
        int hash = hash(key);
        return indexFor(hash, length);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private int hash(K key) {
        return Math.abs(Objects.hashCode(key));
    }
}
