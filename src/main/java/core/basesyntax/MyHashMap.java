package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;
    private int capacity;

    public MyHashMap() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int pos = getIndexFromHash(hash(key));
        if (table[pos] == null) {
            table[pos] = new Node<>(key, value);
            size++;
        } else if (Objects.equals(key, table[pos].key)) {
            table[pos].value = value;
        } else {
            Node<K, V> bucketNode = table[pos];
            while (bucketNode != null) {
                if (bucketNode.next == null) {
                    bucketNode.next = new Node<>(key, value);
                    size++;
                    break;
                }
                if (Objects.equals(key, bucketNode.next.key)) {
                    bucketNode.next.value = value;
                    break;
                }
                bucketNode = bucketNode.next;
            }
        }
    }

    private int getIndexFromHash(int hash) {
        return hash % capacity;
    }

    @Override
    public V getValue(K key) {
        int pos = getIndexFromHash(hash(key));
        for (Node<K, V> x = table[pos]; x != null; x = x.next) {
            if (Objects.equals(key, x.key)) {
                return x.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private int hash(Object key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()));
    }

    private void resize() {
        if (size == LOAD_FACTOR * capacity) {
            Node<K, V>[] oldTable = table;
            table = (Node<K, V>[]) new Node[capacity = capacity << 1];
            putAll(oldTable);
        }
    }

    private void putAll(Node<K, V>[] oldTable) {
        size = 0;
        for (Node<K, V> bucket : oldTable) {
            for (Node<K, V> node = bucket; node != null; node = node.next) {
                put(node.key, node.value);
            }
        }
    }
}
