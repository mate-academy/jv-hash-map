package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.table = new Node [DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key, table.length);
        Node<K, V> node = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = node;
            size++;
        } else {
            putFullBucket(table[index], node, key, value);
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key, table.length);
        Node<K,V> current = table[index];
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

    private int getIndex(K key, int length) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % length);
    }

    private void resize() {
        int newCapacity = table.length * GROW_FACTOR;
        Node<K, V>[] newTable = new Node [newCapacity];
        for (Node<K,V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = getIndex(node.key, newCapacity);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }

    private void putFullBucket(Node<K,V> current, Node<K, V> node, K key, V value) {
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = node;
                size++;
                return;
            }
            current = current.next;
        }
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
