package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MAGNIFICATION_FACTOR = 2;

    private int size;
    private int capacity;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key,value, null);
        int index = getBucketIndex(key);
        if (size > threshold) {
            resize();
        }
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        } else {
            Node<K, V> node = table[index];
            while (node.next != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            node.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getBucketIndex(key)];
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

    private void resize() {
        capacity *= MAGNIFICATION_FACTOR;
        Node<K, V> node;
        Node<K, V>[] previousNode = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> nodes : previousNode) {
            node = nodes;
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
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
