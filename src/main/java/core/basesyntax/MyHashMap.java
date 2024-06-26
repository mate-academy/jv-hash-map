package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }

        int bucketIndex = getBucketIndex(key);
        Node<K, V> head = table[bucketIndex];

        while (head != null) {
            if (Objects.equals(head.key, key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        size++;
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[bucketIndex];
        table[bucketIndex] = newNode;

        if (getLoadFactor() >= DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }

        int bucketIndex = getBucketIndex(key);
        Node<K, V> head = table[bucketIndex];

        while (head != null) {
            if (Objects.equals(head.key, key)) {
                return head.value;
            }
            head = head.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode() % table.length);
    }

    private double getLoadFactor() {
        return (double) size / table.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];

        int oldSize = size;
        size = 0;

        for (Node<K, V> headNode : oldTable) {
            while (headNode != null) {
                put(headNode.key, headNode.value);
                headNode = headNode.next;
            }
        }
        size = oldSize;
    }

    private void putForNullKey(V value) {
        Node<K, V> head = table[0];
        while (head != null) {
            if (head.key == null) {
                head.value = value;
                return;
            }
            head = head.next;
        }
        size++;
        Node<K, V> newNode = new Node<>(null, value);
        newNode.next = table[0];
        table[0] = newNode;

        if (getLoadFactor() >= DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    private V getForNullKey() {
        Node<K, V> node = table[0];
        while (node != null) {
            if (node.key == null) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
