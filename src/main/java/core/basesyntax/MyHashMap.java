package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int INCREAS_COEFF = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int currentCapacity;
    private float thrashLoad;
    private Node<K, V>[] buckets;

    {
        buckets = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        currentCapacity = INITIAL_CAPACITY;
        thrashLoad = currentCapacity * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (size > thrashLoad) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> bucketNode = findNode(key);
        if (bucketNode == null) {
            buckets[hash(key)] = newNode;
            size++;
            return;
        }
        if (key == bucketNode.key || (bucketNode.key != null && bucketNode.key.equals(key))) {
            bucketNode.value = value;
            return;
        }
        bucketNode.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> bucketNode = findNode(key);
        if (bucketNode == null) {
            return null;
        }
        if (key == bucketNode.key || (bucketNode.key != null && bucketNode.key.equals(key))) {
            return bucketNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
            this.hash = key == null ? 0 : Math.abs(key.hashCode());
        }
    }

    private void resize() {
        Node<K, V>[] temporary = (Node<K, V>[]) new Node[currentCapacity * 2];
        fill(temporary);
        buckets = temporary;
        currentCapacity = buckets.length;
        thrashLoad = thrashLoad * 2;
    }

    private Node<K, V> findNode(K key) {
        int position = hash(key);
        if (buckets[position] == null) {
            return null;
        }
        Node<K, V> currentNode = buckets[position];
        while(currentNode.next != null) {
            if (key == currentNode.key || (currentNode.key != null && currentNode.key.equals(key))) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return currentNode;
    }

    private void fill(Node<K, V>[] destination) {
        final int capacity = destination.length;
        int position;
        for (Node<K, V> node: buckets) {
            if (node == null) {
                continue;
            }
            while (node != null) {
                position = node.hash % capacity;
                destination[position] = node;
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % currentCapacity;
    }
}
