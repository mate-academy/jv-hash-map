package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int BASIC_CAPACITY = 16;
    private static final int RESIZE_MULTIPLIER = 2;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[BASIC_CAPACITY];
        threshold = (int) (LOAD_FACTOR * BASIC_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        Node<K, V> indexedNode = table[calculateIndex(key)];
        while (indexedNode != null) {
            if (Objects.equals(key, indexedNode.key)) {
                indexedNode.value = value;
                return;
            }
            if (indexedNode.next == null) {
                indexedNode.next = new Node<>(key, value, null);
                size++;
                return;
            }
            indexedNode = indexedNode.next;
        }
        table[calculateIndex(key)] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> requestNode = table[calculateIndex(key)];
        while (requestNode != null) {
            if (Objects.equals(key, requestNode.key)) {
                return requestNode.value;
            }
            requestNode = requestNode.next;
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

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int calculateIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        int capacity = table.length * RESIZE_MULTIPLIER;
        threshold = capacity * RESIZE_MULTIPLIER;
        size = 0;

        Node<K, V>[] unresized = table;
        table = new Node[capacity];
        for (Node<K, V> node : unresized) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
