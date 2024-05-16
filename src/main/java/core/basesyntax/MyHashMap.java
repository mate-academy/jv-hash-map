package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int RESIZE_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;
    private int capacity = DEFAULT_CAPACITY;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= (capacity * DEFAULT_LOAD_FACTOR)) {
            resize();
        }
        int hash = getHash(key);
        int index = hash % capacity;
        if (this.table[index] == null) {
            this.table[index] = new Node<>(key, value, null, hash);
            size++;
        } else {
            for (Node<K, V> node = this.table[index]; node != null;
                    node = node.next) {
                if (node.hash == hash && Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node(key, value, null, hash);
                    size++;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getHash(key) % capacity;
        for (Node<K, V> currentNode = this.table[index]; currentNode != null;
                currentNode = currentNode.next) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    private int getHash(K key) {

        return key == null ? 0 : ((int)(Math.pow(key.hashCode(), 2)));
    }

    private void resize() {
        this.capacity = capacity * RESIZE_FACTOR;
        size = 0;
        Node<K, V>[] oldTab = this.table;
        this.table = new Node[capacity];
        for (Node<K,V> bucket : oldTab) {
            while (bucket != null) {
                put(bucket.key,bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next, int hash) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = hash;
        }
    }
}
