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
        Node<K, V> newNode = new Node<>(key, value, null);
        int indexOfNode = getIndex(key);
        if (buckets[indexOfNode] == null) {
            buckets[indexOfNode] = newNode;
            size++;
            return;
        }
        Node<K, V> bucketsNode = buckets[indexOfNode];
        while (bucketsNode != null) {
            if (Objects.equals(key, bucketsNode.key)) {
                bucketsNode.value = value;
                return;
            }
            bucketsNode = bucketsNode.next;
        }
        getLastLinkedValue(buckets[indexOfNode]).next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> element = buckets[getIndex(key)];
        while (element != null) {
            if (Objects.equals(element.key, key)) {
                return element.value;
            }
            element = element.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
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
        threshold = (int) (buckets.length * LOAD_FACTOR);
    }

    private Node<K, V> getLastLinkedValue(Node<K, V> node) {
        Node<K, V> element = node;
        while (element.next != null) {
            element = element.next;
        }
        return element;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
