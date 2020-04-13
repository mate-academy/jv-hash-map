package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {

        if (size == buckets.length * LOAD_FACTOR) {
            resize();
        }
        int index = getHashCode(key, buckets.length);
        Node<K,V> newNode = findNode(key, index);
        if (newNode != null) {
            newNode.value = value;
        } else {
            Node<K,V> bucket = new Node(key,value, buckets[index]);
            buckets[index] = bucket;
            size++;
        }
    }

    @Override
    public V getValue(K key) {

        int index = getHashCode(key, buckets.length);
        Node<K,V> bucket = findNode(key, index);
        if (bucket != null) {
            return bucket.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.nextNode = next;
        }
    }

    private int getHashCode(K key, int length) {
        return key == null ? 0 : Math.abs(key.hashCode() % length);
    }

    private Node findNode(K key, int index) {
        Node<K,V> newNod = buckets[index];
        while (newNod != null) {
            if (Objects.equals(key, newNod.key)) {
                return newNod;
            }
            newNod = newNod.nextNode;
        }
        return null;
    }

    private void resize() {
        Node<K, V>[] newNode = new Node[buckets.length * 2];
        size = 0;
        Node<K, V>[] oldBuckets = buckets;
        buckets = newNode;
        for (Node<K, V> bucket : oldBuckets) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.nextNode;
            }
        }
    }
}
