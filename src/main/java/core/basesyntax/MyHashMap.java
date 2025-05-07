package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int threshold;

    private Node<K,V>[] table;

    private int size;

    public MyHashMap() {
        this.table = (Node<K,V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
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
            return Objects.equals(key, node.key) && value.equals(node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K,V> position;
        int index = hash(key) % table.length;
        if ((position = table[index]) == null) {
            table[index] = newNode(hash(key), key, value, null);
            size++;
            return;
        }

        while (position != null) {
            if (Objects.equals(position.key, key)) {
                position.value = value;
                return;
            }
            position = position.next;
        }
        Node<K,V> newNode = new Node<>(hash(key), key, value, table[index]);
        table[index] = newNode;
        size++;
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        return getNode(key) == null ? null : getNode(key).value;
    }

    final Node<K,V> getNode(K key) {
        // get index of bucket
        int index = hash(key) % table.length;
        // iterate inside the bucket
        Node<K,V> existingEntry;
        if (table[index] != null) {
            existingEntry = table[index];
            do {
                if (existingEntry.hash == hash(key)
                        && (existingEntry.key == key
                        || (key != null && key.equals(existingEntry.key)))) {
                    return existingEntry;
                }
            } while ((existingEntry = existingEntry.next) != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        Node<K,V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        int oldThreshold = threshold;
        int newCapacity = oldCapacity << 1;
        threshold = oldThreshold << 1;

        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        table = newTable;
        size = 0;
        for (int j = 0; j < oldCapacity; ++j) {
            Node<K, V> existingEntry = oldTable[j];
            while (existingEntry != null) {
                put(existingEntry.key, existingEntry.value);
                existingEntry = existingEntry.next;
            }
        }

        return newTable;
    }

    private static int hash(Object key) {
        return Math.abs((key == null) ? 0 : key.hashCode());
    }

    private Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
        return new Node<>(hash, key, value, next);
    }
}
