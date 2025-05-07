package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int bucket = getBucket(key);
        if (table[bucket] == null) {
            table[bucket] = new Node<>(key, value);
            size++;
        } else {
            Node<K, V> current = table[bucket];
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = new Node<>(key, value);
                    size++;
                    return;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int bucket = getBucket(key);
        Node<K, V> current = table[bucket];
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

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getBucket(K key) {
        int hash = getHash(key);
        return Math.abs(hash % table.length);
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        table = new Node[oldTable.length << 1];
        threshold = threshold << 1;
        size = 0;
        transfer(oldTable);
    }

    private void transfer(Node<K,V>[] table) {
        for (Node<K, V> node : table) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
