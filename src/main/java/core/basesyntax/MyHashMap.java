package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] storage;
    private int size;

    public MyHashMap() {
        this.storage = new Node[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (putNewValue(key, value)) {
            size++;
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> bucket = storage[index];
        if (bucket == null) {
            return null;
        }
        Node<K, V> node = bucket.findNode(key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (storage.length * DEFAULT_LOAD_FACTOR > size) {
            return;
        }
        Node<K, V>[] oldStorage = storage;
        storage = new Node[oldStorage.length * 2];
        for (Node<K, V> bucket : oldStorage) {
            if (bucket != null) {
                Node<K, V> currentNode = bucket;
                putNewValue(currentNode.key, currentNode.value);
                while (currentNode.next != null) {
                    currentNode = currentNode.next;
                    putNewValue(currentNode.key, currentNode.value);
                }
            }
        }
    }

    private boolean putNewValue(K key, V value) {
        int index = getIndex(key);
        Node<K, V> bucket = storage[index];
        if (bucket == null) {
            storage[index] = new Node<>(key, value);
            return true;
        }
        Node<K, V> node = bucket.findNode(key);
        if (node == null) {
            bucket.addNode(new Node<>(key, value));
            return true;
        }
        node.value = value;
        return false;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode() & storage.length - 1;
    }

    private class Node<K, V> {
        private V value;
        private K key;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        private Node<K, V> findNode(K key) {
            if (Objects.equals(key, this.key)) {
                return this;
            }

            if (this.next == null) {
                return null;
            }
            return this.next.findNode(key);
        }

        private void addNode(Node<K, V> node) {
            if (this.next == null) {
                this.next = node;
            } else {
                this.next.addNode(node);
            }
        }
    }
}
