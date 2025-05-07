package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int SIZE_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int bucketIndex = getBucketIndex(key);
        if (table[bucketIndex] != null) {
            Node<K, V> current = table[bucketIndex];
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            while (current.next != null) {
                current = current.next;
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
            }
            current.next = new Node<K, V>(key, value, null);
            size++;
            return;
        }
        table[bucketIndex] = new Node<K, V>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[getBucketIndex(key)];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resizeIfNeeded() {
        if (size >= table.length * LOAD_FACTOR) {
            Node<K, V>[] oldTable = table;
            size = 0;
            table = (Node<K, V>[]) new Node[table.length * SIZE_MULTIPLIER];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

}
