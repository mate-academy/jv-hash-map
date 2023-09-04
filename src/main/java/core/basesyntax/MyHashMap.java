package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private int size;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int index = getIndex(key);
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        buckets[index] = new Node<>(hash(key), key, value, buckets[index]);
        size++;
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : buckets) {
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        int index = (key == null) ? 0 : (key.hashCode() % buckets.length);
        return index > 0 ? index : Math.abs(index);
    }

    private void resizeIfNeeded() {
        if ((float) size / buckets.length >= LOAD_FACTOR) {
            int newCapacity = buckets.length * 2;
            Node<K, V>[] newBuckets = new Node[newCapacity];
            int newSize = 0;

            for (Node<K, V> node : buckets) {
                while (node != null) {
                    Node<K, V> nextNode = node.next;
                    int newIndex = getIndex(node.key);
                    node.next = newBuckets[newIndex];
                    newBuckets[newIndex] = node;
                    node = nextNode;
                    newSize++;
                }
            }

            buckets = newBuckets;
            size = newSize;
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : (key.hashCode() ^ (key.hashCode() >>> 16));
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
