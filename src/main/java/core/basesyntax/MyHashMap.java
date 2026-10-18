package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int capacity = DEFAULT_CAPACITY;
    private int threshold = (int) (capacity * LOAD_FACTOR);
    private int size = 0;
    private Node<K, V>[] table = new Node[capacity];

    private int calculateIndex(K key) {
        return Math.floorMod(Objects.hashCode(key), capacity);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            Node<K, V>[] oldTable = table;
            capacity *= 2;
            table = new Node[capacity];
            for (Node<K, V> current : oldTable) {
                while (current != null) {
                    Node<K, V> next = current.next;
                    int newIndex = calculateIndex(current.key);

                    current.next = table[newIndex];
                    table[newIndex] = current;

                    current = next;
                }
            }
            threshold = (int) (capacity * LOAD_FACTOR);
        }
        int index = calculateIndex(key);
        Node<K, V> current = table[index];

        if (table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;
        }

        while (current != null) {
            if (Objects.equals(key, current.key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Node<>(key, value);
                size++;
                return;
            }
            current = current.next;
        }

    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
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

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;

        }
    }
}
