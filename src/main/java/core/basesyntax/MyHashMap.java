package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == buckets.length * DEFAULT_LOAD_FACTOR) {
            resizeIfNeeded();
        }
        int index = getIndex(key);
        Node<K, V> currentNode = buckets[index];
        while (currentNode != null) {
            if ((currentNode.key == null && key == null) || Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        buckets[index] = new Node<>(key, value, buckets[index]);
        size++;
    }

    @Override
    public V getValue(K key) {
        if (Objects.isNull(key)) {
            Node<K, V> currentNode = buckets[0];
            while (currentNode != null) {
                if (Objects.isNull(currentNode.key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
            return null;
        }

        int index = getIndex(key);
        Node<K, V> currentNode = buckets[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % buckets.length;
    }

    private void resizeIfNeeded() {
        size = 0;
        Node<K, V>[] previous = buckets;
        buckets = new Node[previous.length * 2];
        for (Node<K, V> node : previous) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
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
