package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= (table.length * LOAD_FACTOR)) {
            resize();
        }
        int bucketIndex = getBucketIndex(key);
        Node<K, V> nodePut = new Node<>(key, value, null);
        Node<K, V> node = table[bucketIndex];
        if (node == null) {
            table[bucketIndex] = nodePut;
        } else {
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    break;
                }
                node = node.next;
            }
            node.next = nodePut;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getBucketIndex(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    private void resize() {
        size = 0;
        int capacity = table.length;
        Node<K, V>[] tempTable = table;
        table = new Node[capacity << 1];
        for (Node<K, V> element : tempTable) {
            while (element != null) {
                put(element.key, element.value);
                element = element.next;
            }
        }
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value,Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
