package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity = INITIAL_CAPACITY;
    private int threshold;
    private Node<K, V>[] table = new Node[capacity];
    private int size;

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getHashCode(key) % capacity;
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(key, value, null);
        }
        for (; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                break;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : table) {
            for (; node != null; node = node.next) {
                if (node.key == key || node.key != null && node.key.equals(key)) {
                    return node.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHashCode(K key) {
        if (key != null) {
            return Math.abs(key.hashCode());
        }
        return 0;
    }

    private void resize() {
        capacity = capacity << 1;
        threshold = Math.round(capacity * LOAD_FACTOR);
        Node<K, V>[] table = this.table;
        this.table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : table) {
            for (; node != null; node = node.next) {
                put(node.key, node.value);
            }
        }
    }

    private class Node<K,V> {
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
