package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int size;
    private Node<K, V>[] table = (Node<K, V>[]) new Node<?,?>[DEFAULT_CAPACITY];
    private Node<K, V> nullKeyNode;

    @Override
    public void put(K key, V value) {
        if (size <= getThreshold()) {
            reSize();
        }
        if (key == null) {
            if (nullKeyNode == null) {
                nullKeyNode = new Node<>(null, value);
                size++;
                return;
            } else {
                nullKeyNode.value = value;
                return;
            }
        }
        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(key, table.length);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    size++;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return nullKeyNode == null ? null : nullKeyNode.value;
        }
        int index = getIndex(key, table.length);
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key != null && current.key.equals(key)) {
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

    private int getThreshold() {
        return (int) DEFAULT_LOAD_FACTOR * table.length;
    }

    private int getIndex(K key, int length) {
        return Math.abs(key.hashCode() % length);
    }

    private void reSize() {
        Node<K, V>[] helperArray = table;
        table = (Node<K, V>[]) new Node<?,?>[table.length * 2];
        for (int i = 0; i < helperArray.length; i++) {
            Node<K, V> current = helperArray[i];
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.hash = key == null ? 0 : key.hashCode();
            this.key = key;
            this.value = value;
            this.next = null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<K, V> current = (Node<K, V>) o;
            return Objects.equals(this.key, current.key)
                    && Objects.equals(this.value, current.value);
        }

        @Override
        public int hashCode() {
            return (key == null) ? 0 : key.hashCode();
        }
    }
}



