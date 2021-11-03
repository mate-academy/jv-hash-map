package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] buckets = new Node[DEFAULT_CAPACITY];
    private int capacity = DEFAULT_CAPACITY;
    private int size;
    private int threshold = (int) (capacity * LOAD_FACTOR);

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndexByKey(key);

        if (size >= threshold) {
            grow();
        }
        if (buckets[index] == null) {
            fillEmptyBucket(newNode, index);
        } else {
            addNextToBucket(newNode, index);
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByKey(key);
        V value;
        Node<K, V> currentNode = buckets[index];

        if (currentNode == null) {
            return null;
        }
        value = currentNode.value;
        while (currentNode.next != null) {
            currentNode = currentNode.next;
            if (Objects.equals(currentNode.key, key)) {
                value = currentNode.value;
            }
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void fillEmptyBucket(Node<K, V> node, int index) {
        buckets[index] = node;
        size++;
    }

    private void addNextToBucket(Node<K, V> node, int index) {
        Node<K, V> currentNode = buckets[index];
        if (isEquals(node, currentNode)) {
            node.next = currentNode.next;
            buckets[index] = node;
        } else {
            while (currentNode.next != null) {
                if (isEquals(node, currentNode.next)) {
                    node.next = currentNode.next.next;
                    currentNode.next = node;
                    return;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = node;
            size++;
        }
    }

    private boolean isEquals(Node<K, V> nodeA, Node<K, V> nodeB) {
        return nodeA.hash == nodeB.hash && Objects.equals(nodeA.key, nodeB.key);
    }

    private void grow() {
        Node<K,V>[] oldBuckets = buckets;
        capacity = capacity << 1;
        buckets = new Node[capacity];
        transfer(oldBuckets);
    }

    private void transfer(Node<K, V>[] oldBuckets) {
        size = 0;
        threshold = (int) (capacity * LOAD_FACTOR);
        for (Node<K, V> currentNode : oldBuckets) {
            if (currentNode != null) {
                put(currentNode.key, currentNode.value);
                while (currentNode.next != null) {
                    currentNode = currentNode.next;
                    put(currentNode.key, currentNode.value);
                }
            }
        }
    }

    private int getIndexByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private final V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.hash = hash(key);
            this.key = key;
            this.value = value;
            this.next = next;
        }

        private int hash(K key) {
            return key == null ? 0 : key.hashCode();
        }
    }
}
