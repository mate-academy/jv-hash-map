package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private Node<K, V>[] table;
    private static final int DEFAULT_CAPACITY = 1 << 4; // 16
    private int size;


    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> entry = new Node<>(key, value, null);
        if (key == null) {
            Node<K, V> existing = table[0];
            if (existing == null) {
                table[0] = entry;
                size++;
            } else {
                putSupplier(key, value, (Node<K, V>) entry, (Node<K, V>) existing);
            }
            return;
        }
        int bucket = getHash(key) % DEFAULT_CAPACITY;
        Node<K, V> existing = table[bucket];
        if (existing == null) {
            table[bucket] = entry;
            size++;
        } else {
            putSupplier(key, value, (Node<K, V>) entry, (Node<K, V>) existing);
        }
    }

    private void putSupplier(K key, V value, Node<K, V> entry, Node<K, V> existing) {
        while (existing.next != null) {
            if (Objects.equals(existing.key, key)) {
                existing.value = value;
                return;
            }
            existing = existing.next;
        }

        if (Objects.equals(existing.key, key)) {
            existing.value = value;
        } else {
            existing.next = entry;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            Node<K, V> bucket = table[0];
            while (bucket != null) {
                if (Objects.equals(bucket.key, key)) {
                    return bucket.value;
                }
                bucket = bucket.next;
            }
        }
        Node<K, V> bucket = table[getHash(key) % DEFAULT_CAPACITY];
        while (bucket != null) {
            if (Objects.equals(bucket.key, key)) {
                return bucket.value;
            }
            bucket = bucket.next;
        }
        return null;
    }

    private int getHash(K key) {
        return Math.abs(key.hashCode());
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
