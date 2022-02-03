package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int size;
    private int capacity;
    private int threshold;
    private final float loadFactor;
    private Node<K, V>[] table;


    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        loadFactor = DEFAULT_LOAD_FACTOR;
        threshold = getActualThreshold(capacity);
        table = (Node<K, V>[])new Node[capacity];
    }

    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node <K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            Node<?, ?> node = (Node<?, ?>) o;
//            return getClass() != o.getClass()
//                    && Objects.equals(key, node.key)
//                    && Objects.equals(value, node.value);
//        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node;
        int hashKey = hash(key);

        if (table[hashKey] == null) {
            table[hashKey] = new Node<>(hashKey, key, value, null);
        } else {
            node = table[hashKey];
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            while (node.next != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            node.next = new Node<>(hashKey, key, value, null);
        }

        if (++size >= threshold) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        capacity <<= 1;
        table = (Node<K, V>[]) new Node[capacity];
        threshold = getActualThreshold(capacity);
        size = 0;
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        Node<K, V> node;
        for (Node<K, V> oldNode : oldTable) {
            if (oldNode == null) {
                continue;
            }
            node = oldNode;
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];
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

    private int getActualThreshold(int capacity) {
        return (int) (capacity * loadFactor);
    }
}
