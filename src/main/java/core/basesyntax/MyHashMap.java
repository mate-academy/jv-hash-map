package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int COEFFICIENT = 2;
    private static final int PRIME_NUMBER = 31;
    private int size;
    private Node<K, V>[] table = new Node[INITIAL_CAPACITY];
    private int threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);

    static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            if (!Objects.equals(key, node.key)) {
                return false;
            }
            return Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            int result = key != null ? key.hashCode() : 0;
            result = PRIME_NUMBER * result + (value != null ? value.hashCode() : 0);
            return result;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        Node<K, V> current = node;
        Node<K, V> prev = null;
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            prev = current;
            current = current.next;
        }
        prev.next = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
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

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * COEFFICIENT];
        threshold = (int) (table.length * LOAD_FACTOR);
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
