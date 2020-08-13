package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] buckets;
    private int size;
    private int threshold;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> node = getNode(key, index);
        if (node != null) {
            node.value = value;
        } else {
            Node<K, V> newNode = new Node<>(key, value, buckets[index]);
            buckets[index] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key, getIndex(key));
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldArray = buckets;
        buckets = new Node[oldArray.length * 2];
        size = 0;
        for (Node<K, V> bucket : oldArray) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % buckets.length;
    }

    private Node<K, V> getNode(K key, int index) {
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

}
