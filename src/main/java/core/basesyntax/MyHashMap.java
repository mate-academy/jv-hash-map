package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        checkForResize();
        int index = getBucketNumber(key);
        Node<K, V> currentNode = buckets[index];
        if (currentNode == null) {
            buckets[index] = new Node<>(key, value);
            size++;
        } else {
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(key, value);
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getBucketNumber(key);
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

    private void resize() {
        final Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[buckets.length * GROW_FACTOR];
        threshold *= GROW_FACTOR;
        size = 0;
        for (Node<K, V> node: oldBuckets) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void checkForResize() {
        if (size >= threshold) {
            resize();
        }
    }

    private int getBucketNumber(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % buckets.length);
    }

    private class Node<K, V> {
        private final K key;
        private V value;

        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
