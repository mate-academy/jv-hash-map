package core.basesyntax;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_INDEX = 2;
    private int size;
    private ArrayList<Node<K, V>>[] buckets = new ArrayList[DEFAULT_CAPACITY];

    @Override
    public void put(K key, V value) {
        int hash = hashCode(key);
        if (buckets[hash] == null) {
            buckets[hash] = new ArrayList<>();
        }
        for (Node<K, V> node : buckets[hash]) {
            if (keyEquals(node.getKey(), key)) {
                node.setValue(value);
                return;
            }
        }
        buckets[hash].add(new Node<>(hash, key, value, null));
        size++;

        if ((double) size / buckets.length >= LOAD_FACTOR) {
            resize();
        }
    }

    private void put(ArrayList<Node<K, V>> bucket, Node<K, V> node) {
        for (Node<K, V> n : bucket) {
            if (keyEquals(n.getKey(), node.getKey())) {
                n.setValue(node.getValue());
                return;
            }
        }
        bucket.add(node);
    }

    @Override
    public V getValue(K key) {
        int hash = hashCode(key);
        List<Node<K, V>> bucket = buckets[hash];
        if (bucket != null) {
            for (Node<K, V> node : bucket) {
                if (keyEquals(node.getKey(), key)) {
                    return node.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int capacity = buckets.length * RESIZE_INDEX;
        ArrayList<Node<K, V>>[] resizeBuckets = new ArrayList[capacity];

        for (ArrayList<Node<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    int newHashCode = hashCode(node.getKey()) % capacity;
                    if (resizeBuckets[newHashCode] == null) {
                        resizeBuckets[newHashCode] = new ArrayList<>();
                    }
                    put(resizeBuckets[newHashCode], node);
                }
            }
        }
        buckets = resizeBuckets;
    }

    private boolean keyEquals(K k1, K k2) {
        if (k1 == null) {
            return k2 == null;
        }
        return k1.equals(k2);
    }

    private int hashCode(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % buckets.length;
    }

    private static class Node<K, V> {
        private final K key;
        private final int hash;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}

