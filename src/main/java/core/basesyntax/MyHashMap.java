package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private float loadFactor;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor) || Float.isInfinite(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be positive and finite");
        }
        this.table = new Node[initialCapacity];
        this.loadFactor = loadFactor;
        this.threshold = (int) (initialCapacity * loadFactor);
        this.size = 0;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
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
            return Objects.equals(key, node.key);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key);
        }
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash);

        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key == null
                    && key == null || current.key != null
                    && current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;

        if (size > threshold) {
            resize(table.length * 2);
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = indexFor(hash);

        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key == null
                    && key == null || current.key != null
                    && current.key.equals(key)) {
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

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private int indexFor(int hash) {
        return hash % table.length;
    }

    private void resize(int newCapacity) {
        Node<K, V>[] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        if (oldCapacity == newCapacity) {
            return;
        }
        Node<K, V>[] newTable = new Node[newCapacity];
        threshold = (int) (newCapacity * loadFactor);
        table = newTable;
        size = 0;
        if (oldTable != null) {
            for (Node<K, V> head : oldTable) {
                while (head != null) {
                    put(head.key, head.value);
                    head = head.next;
                }
            }
        }
    }
}
