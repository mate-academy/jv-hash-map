package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_NUMBER_OF_BUCKETS = 16;
    static final float DEFAULT_LOADER_FACTOR = 0.75f;
    private int numberOfBuckets = DEFAULT_NUMBER_OF_BUCKETS;
    private int size;
    private int index;

    private Node[] buckets = new Node[DEFAULT_NUMBER_OF_BUCKETS];

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > buckets.length * DEFAULT_LOADER_FACTOR) {
            resize();
        }
        index = hashCode(key) % buckets.length;
        Node<K, V> currentNode = buckets[index];
        if (buckets[index] == null) {
            buckets[index] = new Node(hashCode(key), key, value, null);
        } else {
            while ((currentNode.key != key && !currentNode.key.equals(key))
                    && currentNode.next != null) {
                currentNode = currentNode.next;
            }
            if (currentNode.key == key || currentNode.key.equals(key)) {
                currentNode.value = value;
                return;
            }
            if (key == null) {
                currentNode.next = new Node(0, key, value, null);
            } else {
                currentNode.next = new Node(hashCode(key), key, value, null);
            }
        }
        size++;
    }

    private int hashCode(K key) {
        return key == null ? 0 : Math.abs(Objects.hash(key));
    }

    @Override
    public V getValue(K key) {
        index = hashCode(key) % buckets.length;
        Node<K, V> currentNode = buckets[index];
        if (currentNode == null) {
            return null;
        }
        if (key == null) {
            while (currentNode.key != key && currentNode.next != null) {
                currentNode = currentNode.next;
            }
            if (currentNode.next == null && currentNode.key != null) {
                return null;
            }
        } else {
            while ((!currentNode.key.equals(key)) && currentNode.next != null) {
                currentNode = currentNode.next;
            }
            if (currentNode.next == null && !currentNode.key.equals(key)) {
                return null;
            }
        }
        return (V) currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V> currentNode;
        Node[] rewriteBuckets = new Node[size];
        int i = 0;
        for (Node<K, V> oneBucket:buckets) {
            if (oneBucket != null) {
                rewriteBuckets[i] = oneBucket;
                i++;
            }
        }
        numberOfBuckets = buckets.length * 2;
        buckets = new Node[numberOfBuckets];
        for (Node<K, V> oneBucket:rewriteBuckets) {
            if (oneBucket != null) {
                currentNode = oneBucket;
                while (currentNode != null) {
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                    size--;
                }
            }
        }
    }
}

