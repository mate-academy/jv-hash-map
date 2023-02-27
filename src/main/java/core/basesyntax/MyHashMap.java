package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int LENGTH_MULTIPLIER = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.currentValue = value;
                return;
            }
            node = node.next;
        }
        table[index] = new Node<K, V>(key, value, table[index]);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.currentValue;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key != null ? Math.abs(key.hashCode()) % table.length : 0;
    }

    private void resize() {
        if (size >= table.length * LOAD_FACTOR) {
            transfer();
        }
    }

    private void transfer() {
        size = 0;
        Node<K, V>[] nodes = table;
        table = (Node<K, V>[]) new Node[table.length * LENGTH_MULTIPLIER];
        for (Node<K, V> node : nodes) {
            while (node != null) {
                put(node.key, node.currentValue);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V currentValue;
        private Node<K, V> next;

        private Node(K key, V currentValue, Node<K, V> next) {
            this.key = key;
            this.currentValue = currentValue;
            this.next = next;
        }
    }
}
