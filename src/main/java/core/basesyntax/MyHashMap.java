package core.basesyntax;

import static java.lang.Math.abs;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int capacity = INITIAL_CAPACITY;
    private int size = 0;
    private Node<K, V>[] array = (Node<K, V>[]) new Node[INITIAL_CAPACITY];

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.value = value;
            this.key = key;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        putInto(array, key, value);
        resize();
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = array[getHash(key) % capacity];
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

    private void resize() {
        if (size <= capacity * LOAD_FACTOR) {
            return;
        }
        capacity *= 2;
        size = 0;
        Node<K, V>[] newArray = (Node<K, V>[]) new Node[capacity];
        for (int i = 0; i < capacity >> 1; i++) {
            Node<K, V> node = array[i];
            while (node != null) {
                putInto(newArray, node.key, node.value);
                node = node.next;
            }
        }
        array = newArray;
    }

    public void putInto(Node<K, V>[] destination, K key, V value) {
        Node<K, V> node = destination[getHash(key) % destination.length];
        if (node == null) {
            destination[getHash(key) % destination.length] = new Node<>(key, value, null);
            size++;
        } else {
            while (true) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value, null);
                    size++;
                    return;
                }
                node = node.next;
            }
        }
    }

    private int getHash(K key) {
        if (key != null) {
            return abs(key.hashCode());
        }
        return 0;
    }
}
