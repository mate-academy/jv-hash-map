package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_LENGTH = 16;
    private static final int RESIZE_CONSTANT = 2;
    private int size;
    private Node<K, V>[] data;

    public MyHashMap() {
        data = new Node[DEFAULT_LENGTH];
    }

    @Override
    public void put(K key, V value) {
        if (size >= data.length * LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> node = data[index];
        if (node == null) {
            data[index] = newNode;
        }
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                break;
            }
            node = node.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> bucket = data[getIndex(key)];
        while (bucket != null) {
            if (Objects.equals(bucket.key, key)) {
                return bucket.value;
            }
            bucket = bucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % data.length;
    }

    private int getIndex(K key) {
        return (data.length - 1) & hash(key);
    }

    private void resize() {
        Node<K, V>[] oldData = data;
        data = new Node[data.length * RESIZE_CONSTANT];
        size = 0;
        for (Node<K, V> node : oldData) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
