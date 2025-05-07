package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_INDEX = 2;
    private int size;
    private int threshold;
    private Node<K, V> [] buckets;

    public MyHashMap() {
        buckets = new Node[CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        threshold = (int) (buckets.length * LOAD_FACTOR);
        if (size == threshold) {
            doublingCapacity();
        }
        int index = getIndex(key);
        if (buckets[index] == null) {
            buckets[index] = newNode;
            size++;
            return;
        }
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = buckets[index];
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

    public int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private void doublingCapacity() {
        size -= size;
        int newCapacity = buckets.length * RESIZE_INDEX;
        Node<K, V>[] oldBuckets = buckets;
        buckets = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> node: oldBuckets) {
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

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
