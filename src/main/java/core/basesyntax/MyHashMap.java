package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_OF_MAP = 0.75f;
    private Node<K, V>[] fieldOfMap;
    private int size;

    public MyHashMap() {
        this.fieldOfMap = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        checkCapacity();
        int position = hash(key, fieldOfMap.length);
        Node<K, V> node = new Node<>(key, value);
        Node<K, V> current;
        if (fieldOfMap[position] == null) {
            fieldOfMap[position] = node;
            size++;
            return;
        }
        current = fieldOfMap[position];
        do {
            if (Objects.equals(current.key, key)) {
                current.value = node.value;
                return;
            } else if (current.next == null) {
                current.next = node;
                size++;
                return;
            }
            current = current.next;
        } while (current != null);
        current.next = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        return getNode(key) == null ? null : getNode(key).value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkCapacity() {
        int loadFactor = (int) (fieldOfMap.length * LOAD_OF_MAP);
        if (size > loadFactor) {
            grow();
        }
    }

    private void grow() {
        int newCapacity = fieldOfMap.length << 1;
        Node<K, V>[] biggerFieldOfMap = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> kvNode : fieldOfMap) {
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
        fieldOfMap = biggerFieldOfMap;
    }

    private Node<K, V> getNode(K key) {
        int position = hash(key, fieldOfMap.length);
        Node<K, V> current = fieldOfMap[position];
        if (current == null) {
            return null;
        }
        while (current.next != null) {
            if (Objects.equals(current.key, key)) {
                return current;
            }
            current = current.next;
        }
        return current;
    }

    private int hash(Object key, int length) {
        return Math.abs(key == null ? 0 : key.hashCode() % length);
    }
}
