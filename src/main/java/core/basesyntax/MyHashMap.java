package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K,V> [] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    private static class Node<K,V> {
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

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        if (table[getIndex(key)] == null) {
            putInEmptyBucket(key, value);

        } else {
            putInFullBucket(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K,V> node = table[getIndex(key)]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return Math.abs((key == null) ? 0 : key.hashCode());
    }

    private int getIndex(K key) {
        return hash(key) % table.length;
    }

    private void putInEmptyBucket(K key, V value) {
        Node<K,V> newNode = new Node<>(hash(key), key, value, null);
        table[getIndex(key)] = newNode;
        size++;
    }

    private void putInFullBucket(K key, V value) {
        for (Node<K,V> node = table[getIndex(key)]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            } else if (node.next == null) {
                node.next = new Node<>(hash(key), key, value, null);
                size++;
                return;
            }
        }

    }

    private void resize() {
        final Node<K,V>[] oldTable = table;
        size = 0;
        threshold *= 2;
        table = (Node<K, V>[]) new Node[table.length * 2];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
