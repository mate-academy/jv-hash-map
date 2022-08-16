package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int START_CAPACITY = 16;
    private static final int DEFAULT_ARRAY_EXPANSION = 2;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[START_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (buckets[index] == null) {
            buckets[index] = newNode;
            size++;
            return;
        }
        Node<K, V> temporaryNode = buckets[index];
        Node prev = null;
        while (temporaryNode != null) {
            if (Objects.equals(temporaryNode.key, key)) {
                temporaryNode.value = newNode.value;
                return;
            }
            prev = temporaryNode;
            temporaryNode = temporaryNode.next;
        }
        prev.next = newNode;
        size++;

    }

    @Override
    public V getValue(K key) {
        Node<K, V> temporaryNode = buckets[getIndex(key)];
        while (temporaryNode != null) {
            if (Objects.equals(key, temporaryNode.key)) {
                return temporaryNode.value;
            }
            temporaryNode = temporaryNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkSize() {
        if (size >= buckets.length * LOAD_FACTOR) {
            Node<K, V>[] oldBuckets = buckets;
            size = 0;
            Node<K, V>[] newBuckets = new Node[buckets.length * DEFAULT_ARRAY_EXPANSION];
            buckets = newBuckets;
            for (Node<K, V> oldBucket : oldBuckets) {
                while (oldBucket != null) {
                    put(oldBucket.key, oldBucket.value);
                    oldBucket = oldBucket.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % buckets.length);
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
