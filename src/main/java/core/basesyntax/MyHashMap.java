package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROWTH_FACTOR = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        if (table[hash(key)] == null) {
            table[hash(key)] = new Node<>(key, value, null);
            size++;
        } else {
            putNode(key,value);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    private int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void putNode(K key, V value) {
        Node<K, V> node = table[hash(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                break;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                break;
            }
            node = node.next;
        }
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        table = new Node[table.length * GROWTH_FACTOR];
        threshold = (int) (LOAD_FACTOR * table.length);
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
