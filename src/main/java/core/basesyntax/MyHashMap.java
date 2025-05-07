package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K,V>[] storage;
    private int size;

    public MyHashMap() {
        storage = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNecessary();
        int index = getIndex(key);
        Node<K,V> newNode = new Node<>(key, value, null);
        Node<K,V> current = storage[index];
        if (current == null) {
            storage[index] = newNode;
        } else {
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    break;
                }
                current = current.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> current = storage[index];
        if (current == null) {
            return null;
        }
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % storage.length);
    }

    private void resizeIfNecessary() {
        if (size < storage.length * DEFAULT_LOAD_FACTOR) {
            return;
        }
        transfer();
    }

    private void transfer() {
        Node<K,V>[] oldStorage = storage;
        storage = new Node[storage.length * CAPACITY_MULTIPLIER];
        size = 0;
        for (Node<K,V> node : oldStorage) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
