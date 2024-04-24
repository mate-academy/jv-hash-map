package core.basesyntax;

import java.util.ArrayList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private ArrayList<Node<K, V>>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new ArrayList[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        if (buckets[bucketIndex] == null) {
            buckets[bucketIndex] = new ArrayList<>();
        }
        if (buckets[bucketIndex] != null) {
            for (Node<K, V> element : buckets[bucketIndex]) {
                if (element.key != null && element.key.equals(key)) {
                    element.value = value;
                    return;
                } else if (element.key == null && key == null) {
                    element.value = value;
                    return;
                }
            }
        }
        Node<K, V> node = new Node<>();
        node.key = key;
        node.value = value;
        buckets[bucketIndex].add(node);
        size++;

        if (size > buckets.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key);
        if (buckets[bucketIndex] != null) {
            for (Node<K, V> element : buckets[bucketIndex]) {
                if (element.key != null && element.key.equals(key)) {
                    return element.value;
                } else if (element.key == null && key == null) {
                    return element.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucketIndex(K key) {
        return (key == null) ? 0 :
                (key.hashCode() < 0) ? key.hashCode() % buckets.length * (-1) :
                        key.hashCode() % buckets.length;
    }

    private void resize() {
        int oldSize = buckets.length;
        int newSize = oldSize * GROW_FACTOR;
        ArrayList<Node<K, V>>[] newBuckets = new ArrayList[newSize];
        System.arraycopy(buckets, 0, newBuckets, 0, oldSize);
        buckets = newBuckets;
    }

    private static class Node<K, V> {
        private K key;
        private V value;

    }
}
