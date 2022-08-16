package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int INCREASE_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private int currentCapacity;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        currentCapacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        int keyHash = getHash(key);
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> oldNode = null;
        if (table[keyHash] == null) {
            table[keyHash] = newNode;
            size++;
        } else {
            Node<K, V> node = table[keyHash];
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = newNode.value;
                    return;
                }
                oldNode = node;
                node = node.next;
            }
            oldNode.next = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int keyHash = getHash(key);
        Node<K, V> node = table[keyHash];
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

    private int getHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % currentCapacity);
    }

    private void checkSize() {
        if (size + 1 > threshold) {
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * INCREASE_FACTOR];
            threshold = threshold * INCREASE_FACTOR;
            size = 0;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
