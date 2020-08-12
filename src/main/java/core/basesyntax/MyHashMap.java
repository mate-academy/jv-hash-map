package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] buckets;
    private float threshold;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int position = calculateBucket(key);
        Node<K, V> element = findNode(key, position);
        if (element != null) {
            element.value = value;
        } else {
            Node<K, V> entry = new Node<>(key, value, buckets[position]);
            buckets[position] = entry;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> element = findNode(key, calculateBucket(key));
        if (element != null) {
            return element.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> findNode(K key, int index) {
        Node<K, V> element = buckets[index];
        while (element != null) {
            if (Objects.equals(key, element.key)) {
                return element;
            }
            element = element.next;
        }
        return null;
    }

    private int calculateBucket(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % buckets.length;
    }

    private void resize() {
        Node<K, V>[] oldBucket = buckets;
        buckets = new Node[oldBucket.length * 2];
        size = 0;
        for (Node<K, V> bucket : oldBucket) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private final Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
