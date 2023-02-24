package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_SIZE = 16;
    public static final double RESIZE_FACTOR = 0.75;
    public static final double EXPANSION_COEFFICIENT = 2;
    private Node<K, V>[] storage;
    private int size;

    public MyHashMap() {
        storage = (Node<K, V>[]) new Node[DEFAULT_SIZE];
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        if (key == null) {
            if (storage[0] == null) {
                storage[0] = new Node<>(null, value);
                size++;
                return;
            }
            if (storage[0].key == null) {
                storage[0].value = value;
                return;
            }
            System.arraycopy(storage, 0, storage, 1, storage.length - 1);
            storage[0] = new Node<>(null, value);
            size++;
            return;
        }
        for (Node<K, V> node : storage) {
            if (node == null) {
                break;
            }
            if (node.key == null) {
                continue;
            }
            if (Objects.equals(node.key.hashCode(), key.hashCode())) {
                for (Node<K, V> nextNode = node; true; nextNode = nextNode.next) {
                    if (Objects.equals(node.key, key)) {
                        nextNode.value = value;
                        return;
                    }
                    if (nextNode.next == null) {
                        nextNode.next = new Node<>(key, value);
                        size++;
                        return;
                    }
                }

            }
        }
        storage[size++] = new Node<>(key, value);
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : storage) {
            if (node == null) {
                break;
            }
            if (key == null) {
                if (storage[0].key == null) {
                    return storage[0].value;
                }
                return null;
            }
            if (node.key == null) {
                continue;
            }
            if (Objects.equals(node.key.hashCode(), key.hashCode())) {
                for (Node<K, V> nextNode = node; nextNode != null; nextNode = nextNode.next) {
                    if (Objects.equals(nextNode.key, key)) {
                        return nextNode.value;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkSize() {
        if (size / (double) storage.length > RESIZE_FACTOR) {
            int newCapacity = (int) (storage.length * EXPANSION_COEFFICIENT);
            Node<K, V>[] newStorage = (Node<K, V>[]) new Node[newCapacity];
            System.arraycopy(storage, 0, newStorage, 0, storage.length);
            storage = newStorage;
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
