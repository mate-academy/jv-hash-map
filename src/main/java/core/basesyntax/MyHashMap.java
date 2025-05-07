package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] field;
    private int size;

    public MyHashMap() {
        field = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkCapacity();
        int position = hash(key, field.length);
        if (field[position] == null) {
            field[position] = new Node<>(key, value);
            size++;
            return;
        }
        Node<K, V> current = field[position];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
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
        Node<K, V> current = getNode(key);
        return current == null ? null : current.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkCapacity() {
        int loadFactor = (int) (field.length * LOAD_FACTOR);
        if (size > loadFactor) {
            grow();
        }
    }

    private void grow() {
        int newCapacity = field.length << 1;
        Node<K, V>[] biggerFieldOfMap = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> kvNode : field) {
            Node<K, V> current = kvNode;
            while (current != null) {
                do {
                    Node<K, V> next = current.next;
                    int newPosition = hash(current.key, newCapacity);
                    current.next = biggerFieldOfMap[newPosition];
                    biggerFieldOfMap[newPosition] = current;
                    current = next;
                } while (current != null);
            }
        }
        field = biggerFieldOfMap;
    }

    private Node<K, V> getNode(K key) {
        int position = hash(key, field.length);
        Node<K, V> current = field[position];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    private int hash(Object key, int length) {
        return Math.abs(key == null ? 0 : key.hashCode() % length);
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
