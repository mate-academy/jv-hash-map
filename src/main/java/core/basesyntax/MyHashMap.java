package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(hashGen(key), key, value, null);
        Node<K, V> bucket = buckets[findBucket(key)];
        if (bucket == null) {
            buckets[findBucket(key)] = newNode;
            size++;
            return;
        }
        while (bucket.next != null && !Objects.equals(bucket.key, newNode.key)) {
            bucket = bucket.next;
        }
        if (Objects.equals(bucket.key, newNode.key)) {
            bucket.value = newNode.value;
            return;
        }
        bucket.next = newNode;
        size++;

    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        Node<K, V> nodeToFind = buckets[findBucket(key)];
        while (nodeToFind != null) {
            if (Objects.equals(key, nodeToFind.key)) {
                return nodeToFind.value;
            }
            nodeToFind = nodeToFind.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resize() {
        if (size < buckets.length * LOAD_FACTOR) {
            return;
        }
        Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[oldBuckets.length * 2];
        size = 0;
        for (Node<K, V> bucket : oldBuckets) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private int hashGen(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int findBucket(K key) {
        return Math.abs(hashGen(key) % buckets.length);
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
