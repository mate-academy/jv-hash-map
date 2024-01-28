package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        int hash = hash(key);
        int index = hash % table.length;
        Node<K, V> node = table[index];

        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }

            node = node.next;
        }

        addNode(index, new Node<>(hash, key, value));
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = hash % table.length;
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

    private void addNode(int index, Node<K,V> node) {
        node.next = table[index % table.length];
        table[index % table.length] = node;
        size++;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length << 1];
        size = 0;
        threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);

        for (Node<K, V> current : oldTable) {
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
