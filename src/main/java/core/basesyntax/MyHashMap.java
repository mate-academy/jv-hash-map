package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INCREASE_VALUE = 2;
    private Node<K,V>[] bucket;
    private int size;

    public MyHashMap() {
        bucket = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int bucketIndex = getBucketIndex(key);
        Node<K,V> node = bucket[bucketIndex];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K,V> newNode = new Node<>(key,value,bucket[bucketIndex]);
        bucket[bucketIndex] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K,V> node = bucket[bucketIndex];
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

    private void resize() {
        if (size >= LOAD_FACTOR * bucket.length) {
            Node<K, V>[] oldBucket = bucket;
            bucket = new Node[oldBucket.length * INCREASE_VALUE];
            size = 0;
            for (Node<K, V> headNode : oldBucket) {
                while (headNode != null) {
                    put(headNode.key, headNode.value);
                    headNode = headNode.next;
                }
            }
        }
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % bucket.length;
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K,V> next) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}
