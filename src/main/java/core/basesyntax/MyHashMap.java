package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private Node<K, V>[] table;
    private int realCapacity;
    private int threshold;
    private int size;

    MyHashMap() {
        realCapacity = DEFAULT_CAPACITY;
        threshold = (int) (realCapacity * DEFAULT_LOAD_FACTOR);
        table = new Node[realCapacity];
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value);
        int bucket = getBucketNumber(key);
        if (table[bucket] == null) {
            table[bucket] = newNode;
            size++;
            return;
        }
        Node<K, V> currentNode = getNodeFromCollision(table[bucket], key);
        if (Objects.equals(currentNode.key, key)) {
            currentNode.value = newNode.value;
        } else {
            currentNode.next = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int bucket = getBucketNumber(key);
        if (table[bucket] == null) {
            return null;
        }
        Node<K, V> currentNode = getNodeFromCollision(table[bucket], key);
        return Objects.equals(currentNode.key, key) ? currentNode.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNodeFromCollision(Node<K, V> node, K key) {
        while (node.next != null) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return node;
    }

    private int getBucketNumber(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % realCapacity);
    }

    private void resize() {
        size = 0;
        realCapacity = realCapacity << 1;
        threshold = (int) (realCapacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[realCapacity];
        for (Node<K, V> pair : oldTable) {
            if (pair != null) {
                while (pair != null) {
                    put(pair.key, pair.value);
                    pair = pair.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
