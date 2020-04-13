package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_ARRAY_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        size = 0;
        buckets = new Node[DEFAULT_ARRAY_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int index = getBucketIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new Node(key, value);
            size++;
            return;
        }
        if (buckets[index].value == null) {
            buckets[index] = new Node(key, value);
            size++;
        } else {
            setBucket(index, key, value);
        }
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        return findValue(index, key);
    }

    @Override
    public int getSize() {
        return size;
    }

    private void setBucket(int index, K key, V value) {
        Node<K, V> currentNode = buckets[index];
        if (Objects.equals(key, currentNode.key)) {
            currentNode.value = value;
            return;
        }
        while (currentNode.next != null) {
            if (Objects.equals(key, currentNode.next.key)) {
                currentNode.next.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        Node<K, V> entry = new Node(key, value);
        currentNode.next = entry;
        size++;
    }

    private V findValue(int index, K key) {
        if (buckets[index] == null) {
            return null;
        }
        Node<K, V> currentNode = buckets[index];
        do {
            if (Objects.equals(key, currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        } while (currentNode != null);
        return null;
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % (buckets.length));
    }

    private void resizeIfNeeded() {
        if (size < buckets.length * LOAD_FACTOR) {
            return;
        }
        Node<K, V>[] oldData = buckets;
        buckets = new Node[buckets.length * 2];
        size = 0;
        for (int i = 0; i < oldData.length; i++) {
            if (oldData[i] != null) {
                do {
                    put(oldData[i].key, oldData[i].value);
                    oldData[i] = oldData[i].next;
                } while (oldData[i] != null);
            }
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(V value) {
            this.value = value;
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
