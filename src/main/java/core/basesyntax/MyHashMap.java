package core.basesyntax;

import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (!addToTable(key, value)) {
            return;
        }

        if (++size > (int) (table.length * LOAD_FACTOR)) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node<K, V> bucket = table[getBucketIndex(hash)];
        while (bucket != null) {
            if (
                    bucket.key == key
                    || (key != null && key.equals(bucket.key))
            ) {
                return bucket.value;
            }
            bucket = bucket.next;
        }
        return null;
    }

    @Override
    public V remove(K key) {
        if (!isEmpty()) {
            int hash = hash(key);
            int bucketIndex = getBucketIndex(hash);
            Node<K, V> prev = null;
            Node<K, V> bucket = table[bucketIndex];
            while (bucket != null) {
                if (
                        bucket.key == key
                        || (key != null && key.equals(bucket.key))
                ) {
                    if (prev == null) {
                        table[bucketIndex] = bucket.next;
                    } else {
                        prev.next = bucket.next;
                    }
                    bucket.next = null;
                    --size;
                    return bucket.value;
                }
                prev = bucket;
                bucket = bucket.next;
            }
        }
        return null;
    }

    @Override
    public List<K> getKeys() {
        K[] keys = (K[]) new Object[size];
        for (int i = 0, j = 0; i < table.length; i++) {
            Node<K, V> bucket = table[i];
            while (bucket != null) {
                keys[j++] = bucket.key;
                bucket = bucket.next;
            }
        }
        return List.of(keys);
    }

    @Override
    public List<V> getValues() {
        V[] keys = (V[]) new Object[size];
        for (int i = 0, j = 0; i < table.length; i++) {
            Node<K, V> bucket = table[i];
            while (bucket != null) {
                keys[j++] = bucket.value;
                bucket = bucket.next;
            }
        }
        return List.of(keys);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getBucketIndex(int hash) {
        return table.length - 1 & hash;
    }

    private boolean addToTable(K key, V value) {
        int hash = hash(key);
        int bucketIndex = getBucketIndex(hash);
        Node<K, V> bucket = table[bucketIndex];
        if (bucket == null) {
            table[bucketIndex] = new Node<>(key, value, null);
        } else {
            Node<K, V> prev = null;
            while (bucket != null) {
                if (
                        bucket.key == key
                        || (bucket.key != null && bucket.key.equals(key))
                ) {
                    bucket.value = value;
                    return false;
                }
                prev = bucket;
                bucket = bucket.next;
            }
            prev.next = new Node<>(key, value, null);
        }

        return true;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        for (Node<K, V> bucket : oldTable) {
            while (bucket != null) {
                addToTable(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private static final class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
