package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int CAPACITY_MULTIPLIER = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] array;

    public MyHashMap() {
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        array = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        int bucketIndex = getBucketIndex(key);
        Node<K, V> node = array[bucketIndex];
        if (node == null) {
            array[bucketIndex] = new Node<>(key, value, null);
            size++;
            return;
        }
        Node<K, V> lastNode;
        do {
            if (key == node.key || key != null && key.equals(node.key)) {
                node.value = value;
                return;
            }
            lastNode = node;
            node = node.next;
        } while (node != null);
        lastNode.next = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> node = array[bucketIndex];
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
        size = 0;
        Node<K, V>[] oldArray = array;
        array = new Node[array.length * CAPACITY_MULTIPLIER];
        threshold = (int) (array.length * LOAD_FACTOR);
        for (Node<K, V> node : oldArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void checkSize() {
        if (size == threshold) {
            resize();
        }
    }

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % array.length;
    }

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
}
