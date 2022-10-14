package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_RESIZE_COEFFICIENT = 2;
    private Node<K, V>[] bucketArray;
    private int load;
    private int size;

    public MyHashMap() {
        this.bucketArray = new Node[DEFAULT_CAPACITY];
        this.load = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getHash(K key) {
        return (key == null) ? 0 : key.hashCode() % bucketArray.length;
    }

    private int getIndex(K key) {
        return getHash(key) & bucketArray.length - 1;
    }

    private void resize() {
        if (load == size) {
            size = 0;
            Node<K, V>[] oldBucketArray = bucketArray;
            bucketArray = new Node[bucketArray.length * DEFAULT_RESIZE_COEFFICIENT];
            load *= DEFAULT_RESIZE_COEFFICIENT;
            for (Node<K, V> eachBucket: oldBucketArray) {
                while (eachBucket != null) {
                    put(eachBucket.key, eachBucket.value);
                    eachBucket = eachBucket.next;
                }
            }
        }
    }

    public boolean isHashAndKeysEquals(Node<K,V> bucket, int hash, K key) {
        return (bucket.hash == hash && Objects.equals(bucket.key, key));
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = getHash(key);
        int index = getIndex(key);
        Node<K, V> newBucket = new Node<>(hash, key, value, null);
        Node<K,V> bucket = bucketArray[index];
        if (bucket == null) {
            bucketArray[index] = newBucket;
        }
        while (bucket != null) {
            if (isHashAndKeysEquals(bucket, hash, key)) {
                bucket.value = value;
                return;
            }
            if (bucket.next == null) {
                bucket.next = newBucket;
                break;
            }
            bucket = bucket.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        int index = getIndex(key);
        Node<K,V> bucket = bucketArray[index];
        while (bucket != null) {
            if (isHashAndKeysEquals(bucket, hash, key)) {
                return bucket.value;
            }
            bucket = bucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
