package core.basesyntax;

import java.util.Objects;

class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = indexFor(key);
        Node<K, V> node = table[index];

        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }

        addNode(key, value, index);
    }

    @Override
    public V getValue(K key) {
        int index = indexFor(key);
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

    private void addNode(K key, V value, int index) {
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;

        if (size > threshold) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        threshold = (int) (table.length * LOAD_FACTOR);

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                int index = getIndex(node.key, table.length);
                Node<K, V> next = node.next;
                node.next = table[index];
                table[index] = node;
                node = next;
            }
        }
    }

    private int getIndex(K key, int tableLength) {
        int hash = key == null ? 0 : key.hashCode();
        return Math.abs(hash) % tableLength;
    }

    private int indexFor(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        return Math.abs(hash == Integer.MIN_VALUE ? 0 : hash) % table.length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
