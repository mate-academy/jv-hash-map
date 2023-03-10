package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int currentCapacity;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        currentCapacity = INITIAL_CAPACITY;
        threshold = (int) (currentCapacity * LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        int index = getBucket(key);
        if (size > threshold) {
            resizeTable();
        }
        if (table[index] == null) {
            table[index] = node;
        } else {
            Node<K, V> oldNode = table[index];
            while (oldNode != null) {
                if (key == null && oldNode.key == null || key != null && key.equals(oldNode.key)) {
                    oldNode.value = value;
                    return;
                }
                if (oldNode.next == null) {
                    oldNode.next = node;
                    break;
                }
                oldNode = oldNode.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getBucket(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (key == null && node.key == null || node.key != null && node.key.equals(key)) {
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

    private void resizeTable() {
        size = 0;
        currentCapacity *= 2;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[currentCapacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold = (int) (currentCapacity * LOAD_FACTOR);
    }

    private int getBucket(K key) {
        return key == null ? 0 : Math.abs(Objects.hash(key) % currentCapacity);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.hash = key == null ? 0 : Math.abs(Objects.hash(key));
            this.key = key;
            this.value = value;
        }
    }
}
