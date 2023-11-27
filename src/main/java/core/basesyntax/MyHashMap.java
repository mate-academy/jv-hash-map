package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private final Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> 16));
    }


    @Override
    public void put(K key, V value) {
        grow();
        int index = hash(key) % DEFAULT_CAPACITY;
        if (buckets[index] == null) {
            buckets[index] = new Node<>(index, key, value, null);
            size++;
        } else if (buckets[index] != null && (Objects.equals(key, buckets[index].key))) {
            buckets[index] = new Node<>(index, key, value, buckets[index].next);
        } else {
            buckets[index].next = new Node<>(index, key, value, null);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> bucket : buckets) {
            for (Node<K, V> currentNode = bucket; currentNode != null; currentNode = currentNode.next) {
                if (Objects.equals(key, currentNode.key)) {
                    return currentNode.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void grow() {
        if ((double) size / DEFAULT_CAPACITY >= LOAD_FACTOR) {

        }
    }

    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
