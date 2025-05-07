package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int)(DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = calculateIndexByHashcode(key);
        if (table[index] != null) {
            addPair(key, value, index);
        } else {
            table[index] = new Node<>(key, value, null);
            size++;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * MULTIPLIER];
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                Node<K,V> node = oldTable[i];
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
        threshold = (int)(table.length * LOAD_FACTOR);
    }

    private void addPair(K key, V value, int index) {
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;
    }

    private int calculateIndexByHashcode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndexByHashcode(key);
        Node<K, V> node = table[index];
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
}
