package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int capacity;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        putValue(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node<K, V> current = table[hash];
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

    private void putValue(int hash, K key, V value) {
        if (size >= threshold) {
            resize();
        }

        Node<K, V> newNode = new Node<>(hash, key, value, null);
        Node<K, V> current = table[hash];

        if (current == null) {
            table[hash] = newNode;
        } else {
            while (true) {
                if (Objects.equals(current.key, newNode.key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    private int hash(Object key) {
        if (key == null) {
            return 0;
        }
        return (Math.abs(Objects.hashCode(key))) % table.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = (Node<K, V>[]) new Node[capacity];
        System.arraycopy(table, 0, oldTable, 0, table.length);
        capacity = capacity << 1;
        threshold = threshold << 1;
        size = 0;
        table = (Node<K, V>[]) new Node[capacity];

        for (Node<K, V> node : oldTable) {
            Node<K, V> current = node;
            while (current != null) {
                putValue(hash(current.key), current.key, current.value);
                current = current.next;
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
