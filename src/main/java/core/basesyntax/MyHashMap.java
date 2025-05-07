package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = bucketIndex(key);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = newNode;
            size++;
        } else {
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    break;
                } else if (node.next == null) {
                    node.next = newNode;
                    size++;
                }
                node = node.next;
            }
        }
        if (size == threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0 || table == null) {
            return null;
        }
        int index = bucketIndex(key);
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

    private static class Node<K, V> {
        private final K key;
        private Node<K, V> next;
        private V value;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int bucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length << 1;
        threshold = threshold << 1;
        table = (Node<K, V>[]) new Node[newCapacity];
        size = 0;
        for (Node<K, V> bucket : oldTable) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }
}
