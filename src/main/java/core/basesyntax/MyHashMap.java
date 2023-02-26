package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
        this.threshold = (int)(LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int bucketIndex = getBucketIndex(hash);
        Node<K, V> bucket = table[bucketIndex];
        if (bucket == null) {
            table[bucketIndex] = new Node<>(hash, key, value, null);
        } else {
            while (bucket.next != null) {
                if (bucket.hash == hash && Objects.equals(bucket.key, key)) {
                    bucket.value = value;
                    return;
                }
                bucket = bucket.next;
            }
            if (bucket.hash == hash && Objects.equals(bucket.key, key)) {
                bucket.value = value;
                return;
            }
            bucket.next = new Node<>(hash, key, value, null);
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> bucket = table[getBucketIndex(hash(key))];
        while (bucket != null) {
            if (Objects.equals(bucket.key, key)) {
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

    private void resize() {
        int newCapacity = table.length << 1;
        Node<K, V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[newCapacity];
        threshold = (int)(LOAD_FACTOR * newCapacity);
        transferAllNodesToTable(oldTable);
        oldTable = null;
    }

    private void transferAllNodesToTable(Node<K, V>[] oldTable) {
        Node<K, V>[] entries = (Node<K,V>[]) new Node[size];
        int counter = 0;
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                entries[counter++] = node;
                Node<K, V> nextNode = node.next;
                while (nextNode != null) {
                    entries[counter++] = nextNode;
                    nextNode = nextNode.next;
                }
            }
        }
        for (Node<K, V> entry : entries) {
            int newbucketIndex = getBucketIndex(entry.hash);
            Node<K, V> bucket = table[newbucketIndex];
            entry.next = null;
            if (bucket == null) {
                table[newbucketIndex] = entry;
            } else {
                while (bucket.next != null) {
                    bucket = bucket.next;
                }
                bucket.next = entry;
            }
        }
    }

    private int hash(K key) {
        int result;
        return (key == null) ? 0 : (result = key.hashCode()) ^ (result >>> 16);
    }

    private int getBucketIndex(int keyHash) {
        return Math.abs(keyHash % table.length);
    }

    private static class Node<K, V> {
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
}
