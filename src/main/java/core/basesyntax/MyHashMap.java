package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] bucketArray;
    private double threshold;
    private int size;

    public MyHashMap() {
        this.bucketArray = new Node[DEFAULT_CAPACITY];
        this.threshold = DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int nodeIndex = indexFor(key);
        Node<K, V> node = findNode(key, nodeIndex);
        if (node != null) {
            node.value = value;
        } else {
            Node<K, V> newNode = new Node<>(key, value, bucketArray[nodeIndex]);
            bucketArray[nodeIndex] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = findNode(key, indexFor(key));
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] newBucketArray = new Node[bucketArray.length * 2];
        threshold = newBucketArray.length * DEFAULT_LOAD_FACTOR;
        size = 0;
        Node<K, V>[] oldBucketArray = bucketArray;
        bucketArray = newBucketArray;
        for (Node<K, V> node : oldBucketArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.nextNode;
            }
        }
    }

    private int indexFor(K key) {
        return hash(key) % bucketArray.length;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private Node findNode(K key, int nodeIndex) {
        Node<K, V> node = bucketArray[nodeIndex];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node;
            }
            node = node.nextNode;
        }
        return null;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> nextNode;

        private Node(K key, V value, Node<K, V> node) {
            this.key = key;
            this.value = value;
            this.nextNode = node;
        }
    }
}
