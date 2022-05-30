package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private int capacity;
    private Node<K, V>[] table;

    public MyHashMap() {
    }

    @Override
    public void put(K key, V value) {
        if (threshold <= size) {
            table = resize();
        }
        int bucket = getBucket(key);
        Node<K, V> node = table[bucket];
        Node<K, V> newNode;
        if (node == null) {
            table[bucket] = new Node<>(getHash(key),key,value, null);
            size++;
            return;
        }
        while (node != null) {
            if (getHash(key) == node.hash && Objects.equals(node.key, key)) {
                node.value = value;
                return;
            } else if (node.next == null) {
                newNode = new Node<>(getHash(key),key,value, null);
                node.next = newNode;
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        if (threshold <= size) {
            table = resize();
        }
        int bucket = getBucket(key);
        Node<K, V> node = table[bucket];
        if (node == null) {
            return null;
        }
        while (node != null) {
            if (getHash(key) == node.hash && Objects.equals(node.key, key)) {
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

    private Node<K, V>[] resize() {
        int oldCapacity = (table == null) ? 0 : table.length;
        if (threshold == 0) {
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
            capacity = DEFAULT_INITIAL_CAPACITY;
            return new Node[capacity];
        }
        Node<K, V>[] newTable;
        capacity = oldCapacity << 1;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        newTable = new Node[capacity];
        for (Node<K, V> kvNode : table) {
            Node<K, V> node = kvNode;
            while (node != null) {
                Node<K, V> newNode = node;
                int bucket = Math.abs(node.hash) % capacity;
                node = node.next;
                newNode.next = null;
                Node<K, V> current = newTable[bucket];
                if (current == null) {
                    newTable[bucket] = newNode;
                    continue;
                }
                while (current.next != null) {
                    current = current.next;
                }
                current.next = newNode;
            }
        }
        return newTable;
    }

    private int getBucket(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        return Math.abs(hash) % capacity;
    }

    private int getHash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }
}
