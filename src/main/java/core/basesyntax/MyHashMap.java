package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75d;
    private int threshold;
    private int initialCapacity;
    private int size;
    private Node[] data;

    public MyHashMap() {
        initialCapacity = DEFAULT_CAPACITY;
        data = new Node[initialCapacity];
        threshold = (int) (initialCapacity * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node currentNode;
        Node newNode = new Node<>(key, value, null);
        int hash = bucketByKey(key);
        if (size >= threshold) {
            resize();
        }
        if (data[hash] == null) {
            data[hash] = newNode;
        } else {
            currentNode = data[hash];
            while (currentNode.next != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node node = data[bucketByKey(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return (V) node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        initialCapacity *= 2;
        Node<K, V> currentNode;
        Node<K, V>[] oldArray = data;
        data = new Node[initialCapacity];
        size = 0;
        for (Node<K, V> node : oldArray) {
            currentNode = node;
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
        threshold = (int) (initialCapacity * LOAD_FACTOR);
    }

    private int bucketByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % initialCapacity);
    }

    private class Node<K, V> {
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
