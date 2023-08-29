package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] hashTable;
    private int threshold;
    private int size;

    public MyHashMap() {
        hashTable = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = getBucketIndex(key);
        Node<K, V> node = hashTable[index];
        Node<K, V> newNode = new Node<>(key, value);
        if (node != null) {
            while (node != null) {
                if (Objects.equals(node.key, newNode.key)) {
                    node.value = newNode.value;
                    return;
                }
                node = node.next;
            }
            newNode.next = hashTable[index];
        }
        hashTable[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        Node<K, V> node = hashTable[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    private void resize() {
        int newCapacity = hashTable.length * CAPACITY_MULTIPLIER;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        Node[] temp = hashTable;
        hashTable = new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : temp) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getBucketIndex(K key) {
        return hash(key) % hashTable.length;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;

        }
    }
}
