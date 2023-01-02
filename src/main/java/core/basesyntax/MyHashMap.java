package core.basesyntax;

import java.util.Objects;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int capacity;
    private int threshold;
    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        if (size == 0) {
            capacity = capacity == 0 ? INITIAL_CAPACITY : capacity * 2;
            threshold = (int) (capacity * LOAD_FACTOR);
            table = new Node[capacity];
        }
        Node<K, V> node = new Node<>(key, value);
        if (table[node.hash % capacity] == null) {
            table[node.hash % capacity] = node;
            if (++size > threshold) {
                resize();
            }
        } else {
            Node<K, V> current = table[node.hash % capacity];
            if (Objects.equals(current.key, node.key)) {
                current.value = node.value;
            } else {
                while (current.next != null) {
                    current = current.next;
                    if (Objects.equals(current.key, node.key)) {
                        current.value = node.value;
                        return;
                    }
                }
                current.next = node;
                if (++size > threshold) {
                    resize();
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        Node<K, V> current = table[Math.abs(key == null ? 0 : key.hashCode()) % capacity];
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

    private void resize() {
        Node<K, V>[] oldTable = table;
        size = 0;
        for (Node<K, V> bucket : oldTable) {
            if (bucket != null) {
                Node<K, V> current = bucket;
                while (current != null) {
                    put(current.key, current.value);
                    current = current.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value) {
            this.hash = key == null ? 0 : Math.abs(key.hashCode());
            this.key = key;
            this.value = value;
        }
    }
}
