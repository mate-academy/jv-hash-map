package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int actualCapacity = DEFAULT_INITIAL_CAPACITY;
    private int actualSize = 0;
    private int actualThreshold = (int) ((double) DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private Node<K, V>[] buckets = new Node[actualCapacity];

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        if (existingNode(key) != null) {
            existingNode(key).value = value;
        } else {
            actualSize++;
            if (actualSize >= actualThreshold) {
                resize();
            }
            Node<K, V> checkNode = buckets[hash(key) % actualCapacity];
            if (checkNode != null) {
                while (checkNode.next != null) {
                    checkNode = checkNode.next;
                }
                checkNode.next = node;
            } else {
                buckets[hash(key) % actualCapacity] = node;
            }

        }
    }

    private void resize() {
        Node<K, V>[] oldBuckets = new Node[actualCapacity];
        for (int i = 0; i < actualCapacity; i++) {
            oldBuckets[i] = buckets[i];
        }
        actualCapacity = actualCapacity * 2;
        actualThreshold = actualThreshold * 2;
        buckets = new Node[actualCapacity];
        actualSize = 1;
        for (int i = 0; i < oldBuckets.length; i++) {
            if (oldBuckets[i] != null) {
                Node<K, V> oldNode = oldBuckets[i];
                put(oldBuckets[i].key, oldBuckets[i].value);
                while (oldNode.next != null) {
                    oldNode = oldNode.next;
                    put(oldNode.key, oldNode.value);
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> existingNode = existingNode(key);
        if (existingNode != null) {
            return existingNode.value;
        }
        return null;
    }

    private Node<K, V> existingNode(K key) {
        int bucketNumber = hash(key) % buckets.length;
        Node<K, V> node = buckets[bucketNumber];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node;
            } else {
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return actualSize;
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode());
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final void setValue(V newValue) {
            V oldValue = value;
            value = newValue;
        }
    }
}
