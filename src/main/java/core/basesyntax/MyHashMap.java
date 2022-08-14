package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int RESIZE_MULTIPLIER = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] buckets;
    private int threshold;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        resizeCheck();
        int index = hash(key);
        Node<K, V> node = new Node<>(key, value, null);
        if (buckets[index] == null) {
            buckets[index] = node;
            size++;
        } else {
            Node<K, V> currentNode = buckets[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                } else if (currentNode.next == null) {
                    currentNode.next = node;
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
    }

    private void resizeCheck() {
        if (size == threshold) {
            size = 0;
            Node<K, V>[] oldBuckets = buckets;
            int newCapacity = oldBuckets.length * RESIZE_MULTIPLIER;
            threshold = (int) (DEFAULT_LOAD_FACTOR * newCapacity);
            buckets = new Node[newCapacity];
            for (Node<K, V> node : oldBuckets) {
                while (node != null) {
                    put(node.key,node.value);
                    node = node.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = buckets[hash(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;

        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % buckets.length);
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

