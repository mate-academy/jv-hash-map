package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] buckets;
    private int capacity;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.buckets = new Node[DEFAULT_CAPACITY];
        this.capacity = DEFAULT_CAPACITY;
        this.threshold = (int) (capacity * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        if (size >= threshold) {
            grow();
        }
        int index = getIndexByKey(key);
        if (buckets[index] == null) {
            fillEmptyBucket(newNode, index);
        } else {
            addNextToBucket(newNode, index);
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByKey(key);
        V value;
        Node<K, V> node = buckets[index];
        if (node == null) {
            return null;
        }
        value = node.value;
        while (node.next != null) {
            node = node.next;
            if (Objects.equals(node.key, key)) {
                value = node.value;
            }
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private void fillEmptyBucket(Node<K, V> node, int index) {
        buckets[index] = node;
        size++;
    }

    private void addNextToBucket(Node<K, V> node, int index) {
        Node<K, V> prev = buckets[index];
        if (isEquals(node, prev)) {
            node.next = prev.next;
            buckets[index] = node;
        } else {
            while (prev.next != null) {
                if (isEquals(node, prev.next)) {
                    node.next = prev.next.next;
                    prev.next = node;
                    return;
                }
                prev = prev.next;
            }
            prev.next = node;
            size++;
        }
    }

    private boolean isEquals(Node<K, V> firstNode, Node<K, V> secondNode) {
        return firstNode.hash == secondNode.hash && Objects.equals(firstNode.key, secondNode.key);
    }

    private void grow() {
        Node<K,V>[] oldBuckets = buckets;
        capacity = capacity << 1;
        buckets = new Node[capacity];
        transfer(oldBuckets);
    }

    private void transfer(Node<K, V>[] oldBuckets) {
        size = 0;
        threshold = (int) (capacity * LOAD_FACTOR);
        for (Node<K, V> oldBucket : oldBuckets) {
            if (oldBucket != null) {
                Node<K, V> node = oldBucket;
                put(node.key, node.value);
                while (node.next != null) {
                    node = node.next;
                    put(node.key, node.value);
                }
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.hash = hash(key);
            this.key = key;
            this.value = value;
            this.next = next;
        }

        private int hash(K key) {
            return key == null ? 0 : key.hashCode();
        }
    }
}
