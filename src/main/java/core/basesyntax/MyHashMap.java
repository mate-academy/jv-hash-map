package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] bucket;
    private int size;

    public MyHashMap() {
        bucket = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = indexFor(key);
        if (bucket[index] == null) {
            bucket[index] = new Node<>(key, value);
            size++;
        } else {
            Node<K, V> node = bucket[index];
            while (Objects.equals(node.key, key) || node.next != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            node.next = new Node<>(key, value);
            size++;
        }
        if (size >= bucket.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int index = indexFor(key);
        Node<K, V> node = bucket[index];
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

    private int hashGen(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int indexFor(K key) {
        return Math.abs(hashGen(key) % bucket.length);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldBucket = bucket;
        bucket = new Node[oldBucket.length * 2];
        for (Node<K, V> node : oldBucket) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
