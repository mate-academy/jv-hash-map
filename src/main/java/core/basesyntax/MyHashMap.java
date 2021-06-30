package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_MULTIPLIER = 2;
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
        if (size > capacity * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = array[getIndex(key)];
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
        capacity *= GROW_MULTIPLIER;
        size = 0;
        Node<K, V>[] newArray = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> node : array) {
            while (node != null) {
                putInto(newArray, node.key, node.value);
                node = node.next;
            }
        }
        array = newArray;
    }

    public void putInto(Node<K, V>[] destination, K key, V value) {
        Node<K, V> node = destination[getIndex(key)];
        if (node == null) {
            destination[getIndex(key)] = new Node<>(key, value, null);
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }
}
