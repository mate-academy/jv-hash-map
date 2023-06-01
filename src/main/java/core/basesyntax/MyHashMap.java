package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size;
    private int threshold = (int) Math.round(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key != null && (key == node.key || node.key.equals(key))) {
                node.value = value;
                return;
            }
            if (node.key == null && key == null) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> nodeNew = new Node<>(key, value, table[index]);
        table[index] = nodeNew;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    public void resize() {
        int newCapacity = table.length << 1;
        Node<K, V>[] oldCapacity = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldCapacity) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold = threshold << 1;
    }

    private static class Node<K, V> {
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
