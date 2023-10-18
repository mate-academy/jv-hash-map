package core.basesyntax;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int DEFAUL_CAPACITY = 16;
    private List<LinkedList<Node<K,V>>> buckets;
    private int size;

    public MyHashMap() {
        this(DEFAUL_CAPACITY);
    }

    public MyHashMap(int capacity) {
        buckets = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            buckets.add(new LinkedList<>());
        }
    }

    @Override
    public void put(K key, V value) {
        int indexBucket = getBucketIndex(key);
        LinkedList<Node<K,V>> bucket = buckets.get(indexBucket);

        for (Node<K,V> node : bucket) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
        }

        bucket.add(new Node<>(key, value));
        size++;

        if ((double) size / buckets.size() > DEFAULT_LOAD_FACTOR) {
            resize();
        }

    }

    @Override
    public V getValue(K key) {
        int indexBucket = getBucketIndex(key);
        LinkedList<Node<K,V>> bucket = buckets.get(indexBucket);

        for (Node<K,V> node :bucket) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        List<LinkedList<Node<K,V>>> newBuckets = new ArrayList<>(buckets.size() * 2);
        for (int i = 0; i < buckets.size(); i++) {
            newBuckets.add(new LinkedList<>());
        }

        for (LinkedList<Node<K,V>> bucket : buckets) {
            for (Node<K,V> node : bucket) {
                int newBucketIndex = (node.key == null) ? 0 : getBucketIndex(node.key);
                System.out.println("Node key: " + node.key + ", newBucketIndex: " + newBucketIndex
                        + ", newBuckets.size(): " + newBuckets.size());
                newBuckets.get(newBucketIndex).add(node);
            }
        }
        buckets = newBuckets;
    }

    private int getBucketIndex(K key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        return (hash < 0) ? -hash % buckets.size() : hash % buckets.size();
    }

    private static class Node<K,V> {
        private final K key;
        private V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;;
        }
    }
}
