package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int PRIME_NUMBER_17 = 17;
    private static final int PRIME_NUMBER_31 = 31;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resizeCountOfBuckets();
        }
        int indexOfBucket = getIndexOfBucket(key);
        if (table[indexOfBucket] == null) {
            table[indexOfBucket] = new Node<>(key, value, null);
            size++;
        } else {
            checkingOfTheBucket(table[indexOfBucket], key, value);
        }
    }

    @Override
    public V getValue(K key) {
        int indexOfBucket = getIndexOfBucket(key);
        Node<K, V> entry = table[indexOfBucket];
        while (entry != null) {
            if (Objects.equals(entry.key, key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        return PRIME_NUMBER_31 * PRIME_NUMBER_17 + Math.abs(key == null ? 0 : key.hashCode());
    }

    private void checkingOfTheBucket(Node<K, V> entry, K key, V value) {
        while (entry != null) {
            if (Objects.equals(entry.key, key)) {
                entry.value = value;
                return;
            }
            if (entry.next == null) {
                entry.next = new Node<>(key, value, null);
                size++;
                return;
            }
            entry = entry.next;
        }
    }

    private void resizeCountOfBuckets() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                K key = node.key;
                V value = node.value;
                put(key, value);
                node = node.next;
            }
        }
    }

    private int getIndexOfBucket(K key) {
        if (key == null) {
            return 0;
        }
        return getHash(key) % table.length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

