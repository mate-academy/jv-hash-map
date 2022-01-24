package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float THRESHOLD = 0.75F;
    private int size;
    private Node<K, V>[] buckets;

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> DEFAULT_CAPACITY));
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 == buckets.length * THRESHOLD) {
            resize();
        }
        Node<K, V> node = new Node<>(key, value, null);

        int bucket = hash(key) % buckets.length;

        Node<K, V> existing = buckets[bucket];
        if (existing == null) {
            buckets[bucket] = node;
            size++;
        } else {
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
                existing.next = node;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> bucket = buckets[hash(key) % buckets.length];
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

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void resize() {
        Node[] temp = buckets;
        buckets = new Node[buckets.length * 2];
        size = 0;
        for (Node<K, V> node : temp) {
            if (buckets != null) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
