package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int currentCapacity = DEFAULT_CAPACITY;
    private double threshHold = DEFAULT_CAPACITY * LOAD_FACTOR;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[currentCapacity];
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> 16));
    }


    @Override
    public void put(K key, V value) {
        resize();
        int index = hash(key) % currentCapacity;
        if (buckets[index] == null) {
            buckets[index] = new Node<>(index, key, value, null);
            size++;
        } else if (buckets[index] != null) {
            Node<K, V> currentNode = buckets[index];
            if (Objects.equals(key, currentNode.key) && Objects.equals(value, currentNode.value)) {
                return;
            }
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            for (currentNode = buckets[index]; currentNode.next != null; currentNode = currentNode.next) {
                if (Objects.equals(key, currentNode.key) && Objects.equals(value, currentNode.value)) {
                    return;
                }
                if (Objects.equals(key, currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
            }
            if (Objects.equals(key, currentNode.key) && Objects.equals(value, currentNode.value)) {
                return;
            }
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            currentNode.next = new Node<>(index, key, value, null);
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

    public void resize() {
        if (size == threshHold) {
            size = 0;
            currentCapacity *= 2;
            threshHold *= 2;
            Node<K, V>[] oldBuckets = buckets;
            buckets = new Node[currentCapacity];
            for (Node<K, V> node : oldBuckets) {
                for (Node<K, V> currentNode = node; currentNode != null; currentNode = currentNode.next) {
                    put(currentNode.key, currentNode.value);
                }
            }
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
