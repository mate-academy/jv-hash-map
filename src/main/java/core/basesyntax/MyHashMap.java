package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int RESIZE_COEFFICIENT = 2;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfExcedeCapacity();
        int hash = calculateBucketIndex(key);
        Node<K, V> node = table[hash];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(key, value, table[hash]);
        table[hash] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = calculateBucketIndex(key);
        Node<K, V> node = table[hash];
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

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * RESIZE_COEFFICIENT];
        size = 0;
        for (Node<K, V> current : oldTable) {
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private int calculateBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resizeIfExcedeCapacity() {
        if ((double) size / table.length >= LOAD_FACTOR) {
            resize();
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
