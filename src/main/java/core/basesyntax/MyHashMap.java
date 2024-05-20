package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private boolean isResizeExecuting;
    private int size;
    private int capacity;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        float threshold = capacity * DEFAULT_LOAD_FACTOR;
        if (size > threshold) {
            resize();
        }
        int index = getHash(key);
        Node<K, V> node = table[index];
        if (node == null) {
            Node<K, V> newNode = new Node<>(key, getHash(key), value);
            table[index] = newNode;
            if (!isResizeExecuting) {
                size++;
            }
            return;
        }
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            } else if (!Objects.equals(node.key, key) && node.next == null) {
                node.next = new Node<>(key, getHash(key), value);
                if (!isResizeExecuting) {
                    size++;
                }
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int index = getHash(key);
        Node<K, V> node = table[index];
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

    private int getHash(K key) {
        int keyHashcode = 0;
        if (key != null) {
            keyHashcode = key.hashCode();
        }
        if (key != null && key.hashCode() < 0) {
            keyHashcode = Math.abs(key.hashCode());
        }
        return key == null ? 0 : keyHashcode % capacity;
    }

    private void resize() {
        isResizeExecuting = true;
        Node<K, V>[] oldTable = table;
        int oldCapacity = capacity;
        capacity = capacity * 2;
        Node<K, V>[] newTable = new Node[capacity];
        table = newTable;
        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        isResizeExecuting = false;
    }

    private class Node<K, V> {
        private final K key;
        private final int hash;
        private V value;
        private Node<K, V> next;

        Node(K key, int hash, V value) {
            this.key = key;
            this.hash = hash;
            this.value = value;
        }
    }
}
