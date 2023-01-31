package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final double LOAD_FACTOR = 0.75;
    private int size = 0;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        this.buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value, null);
        int index = index(key);
        if (buckets[index] == null) {
            buckets[index] = node;
            size++;
        } else {
            Node<K, V> previousNode = null;
            Node<K, V> currentNode = buckets[index];
            while (currentNode != null) {
                if (node.key == null && key == null || currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
            if (previousNode != null) {
                previousNode.next = node;
                size++;
            }
            if (size > buckets.length * LOAD_FACTOR) {
                resize();
            }
        }
    }

    @Override
    public V getValue(K key) {
        V value = null;
        int index = index(key);
        Node<K, V> myEntry = buckets[index];
        while (myEntry != null) {
            if (myEntry.key == null && key == null || myEntry.key.equals(key)) {
                value = myEntry.value;
                break;
            }
            myEntry = myEntry.next;
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int tableSize = 2 * buckets.length;
        Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[tableSize];
        size = 0;
        for (Node<K, V> oldBucket : oldBuckets) {
            if (oldBucket != null) {
                Node<K, V> currentNode = oldBucket;
                while (currentNode != null) {
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private int index(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(Objects.hash(key) % buckets.length);
    }

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
}
