package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private Node<K, V>[] hashtable;
    private int size = 0;
    private float threshold;

    public MyHashMap() {
        hashtable = new Node[16];
        threshold = hashtable.length * 0.75f;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getBucket(key, hashtable.length);
        Node<K, V> node = findNode(key, index);
        if (node != null) {
            node.value = value;
        } else {
            Node<K, V> entry = new Node<>(key, value, hashtable[index]);
            hashtable[index] = entry;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = findNode(key, getBucket(key, hashtable.length));
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> findNode(K key, int index) {
        Node<K, V> node = hashtable[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void resize() {
        Node<K, V>[] newBuckets = new Node[hashtable.length * 2];
        threshold = newBuckets.length * 0.75f;
        size = 0;
        Node<K, V>[] oldBuckets = hashtable;
        hashtable = newBuckets;
        for (Node<K, V> bucket : oldBuckets) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private int getBucket(K key, int capacity) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}




