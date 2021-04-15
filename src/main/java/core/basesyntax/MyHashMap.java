package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int THRESHOLD_RESIZE_INDEX = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        Node<K, V> node = new Node<K, V>(key, value,null);
        int index = getBucket(node.key);
        if (table[index] == null) {
            table[index] = node;
            size++;
            return;
        }
        addNode(node);
    }

    @Override
    public V getValue(K key) {
        int index = getBucket(key);
        Node<K, V> current = table[index];
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

    private void addNode(Node<K, V> node) {
        int index = getBucket(node.key);
        Node<K, V> current = table[index];
        while (current.next != null || Objects.equals(current.key, node.key)) {
            if (Objects.equals(current.key, node.key)) {
                current.value = node.value;
                return;
            }
            current = current.next;
        }
        current.next = node;
        size++;
    }

    private int getKeyHash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getBucket(K key) {
        return Math.abs(getKeyHash(key)) % table.length;
    }

    private void checkSize() {
        if (size > threshold) {
            resize();
        }
    }

    private void resize() {
        size = 0;
        int newCapacity = table.length * THRESHOLD_RESIZE_INDEX;
        threshold *= THRESHOLD_RESIZE_INDEX;
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K, V> {
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
