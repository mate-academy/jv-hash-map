package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private int size;

    private LinkedList<Node<K, V>>[] bucketArray;

    public MyHashMap() {
        bucketArray = new LinkedList[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            handleNullKey(value);
            return;
        }

        int bucketIndex = getBucketIndex(key);
        if (bucketArray[bucketIndex] == null) {
            bucketArray[bucketIndex] = new LinkedList<>();
        }

        LinkedList<Node<K, V>> bucket = bucketArray[bucketIndex];
        for (Node<K, V> node : bucket) {
            if (key.equals(node.key)) {
                node.value = value;
                return;
            }
        }

        bucket.add(new Node<>(key, value));
        size++;

        sizeCheck();
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return handleNullKey();
        }

        int bucketIndex = getBucketIndex(key);

        LinkedList<Node<K, V>> bucket = bucketArray[bucketIndex];
        if (bucket != null) {
            for (Node<K, V> node : bucket) {
                if (key.equals(node.key)) {
                    return node.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void handleNullKey(V value) {
        if (bucketArray[0] == null) {
            bucketArray[0] = new LinkedList<>();
            bucketArray[0].add(new Node<>(null, value)); // Add a new node with null key
            size++;
            if ((double) size / bucketArray.length > LOAD_FACTOR) {
                resize();
            }
            return;
        }

        LinkedList<Node<K, V>> bucket = bucketArray[0];
        for (Node<K, V> node : bucket) {
            if (node.key == null) {
                node.value = value;
                return;
            }
        }
        bucket.add(new Node<>(null, value));
        size++;
    }

    private V handleNullKey() {
        LinkedList<Node<K, V>> bucket = bucketArray[0];
        if (bucket != null) {
            for (Node<K, V> node : bucket) {
                if (node.key == null) {
                    return node.value;
                }
            }
        }
        return null;
    }

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % bucketArray.length);
    }

    private void sizeCheck() {
        if ((double) size / bucketArray.length > LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = bucketArray.length * 2;
        LinkedList<Node<K, V>>[] newBuckets = new LinkedList[newCapacity];

        for (LinkedList<Node<K, V>> bucket : bucketArray) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    int newBucketIndex = (node.key == null) ? 0 :
                            Math.abs((node.key.hashCode() % newCapacity));
                    if (newBuckets[newBucketIndex] == null) {
                        newBuckets[newBucketIndex] = new LinkedList<>();
                    }
                    newBuckets[newBucketIndex].add(node);
                }
            }
        }

        bucketArray = newBuckets;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.hash = (key == null) ? 0 : key.hashCode();
            this.key = key;
            this.value = value;
        }
    }
}
