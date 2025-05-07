package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] buckets;
    private double threshold;
    private int size;

    public MyHashMap() {
        buckets = new Node[INITIAL_CAPACITY];
        threshold = INITIAL_CAPACITY * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;
        threshold = (double) size / buckets.length;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = buckets[index];
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

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private void resize() {
        if (threshold >= LOAD_FACTOR) {
            Node<K, V>[] newBuckets = new Node[buckets.length * 2];
            for (Node<K, V> node : buckets) {
                while (node != null) {
                    int newIndex = Math.abs(node.key.hashCode()) % newBuckets.length;
                    Node<K, V> temp = node.next;
                    node.next = newBuckets[newIndex];
                    newBuckets[newIndex] = node;
                    node = temp;
                }
            }
            buckets = newBuckets;
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
