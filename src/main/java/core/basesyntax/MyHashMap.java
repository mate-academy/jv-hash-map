package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int SCALE_FACTOR = 2;
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int bucketNumber = getBucketNumber(key);
        if (table[bucketNumber] == null) {
            table[bucketNumber] = new Node<>(key, value, null);
        } else {
            Node<K, V> bucketNode = table[bucketNumber];
            do {
                if (Objects.equals(bucketNode.key, key)) {
                    bucketNode.value = value;
                    return;
                } else if (bucketNode.next == null) {
                    bucketNode.next = new Node<>(key, value, null);
                    break;
                }
                bucketNode = bucketNode.next;
            } while (bucketNode != null);
        }
        size++;
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getBucketNumber(key)];
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

    private int getBucketNumber(K key) {
        int hashKey = key == null ? 0 : key.hashCode();
        return Math.abs(hashKey % table.length);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        size = 0;
        table = (Node<K, V>[]) new Node[oldTable.length * SCALE_FACTOR];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (var bucketItem : oldTable) {
            while (bucketItem != null) {
                put(bucketItem.key, bucketItem.value);
                bucketItem = bucketItem.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
