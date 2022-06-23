package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    final static int INITIAL_CAPACITY = 16;
    final static float LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    @Override
    public void put(K key, V value) {
        resize(threshold);
        // визначити куди саме покласти нову ноду
        if (key == null) {

        }

        // бакет може бути порожнім
            // створити нову ноду, обчислити хеш, ключ та значення вже є, поле некст = налл

        // бакет може бути зайнятим іншою нодою
            // якщо нода така сам - переписати її значення
            // якщо нода інша - додати ноду до хвосту

        table[hash(key)] = (Node<K, V>) value;

    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> newNode (K key, V value) {
        return new Node<>(hash(key), key, value, null);
    }

    private Node<K, V>[] resize(int threshold) {
        int capacity = 0;
        Node<K, V>[] newTable = table;
        if (newTable == null) {
            capacity = INITIAL_CAPACITY;
            threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
            table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        }
        if (++size > threshold) {
            capacity = capacity * 2;
            threshold = threshold * 2;
            newTable = (Node<K, V>[]) new Node[capacity];
            // transfer(newTable);
        }
        return newTable;
    }

    private Node<K, V>[] transfer (Node<K, V>[] newTable) {
        for (Node<K, V> node : table) {
            if (node != null) {
                hash(node.key);
            }

        }
        table = newTable;
        return table;
    }

    private final int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return hash == node.hash
                    && Objects.equals(key, node.key)
                    && Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hash, key, value, next);
        }
    }
}
