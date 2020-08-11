package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K,V>[] table;
    private int size = 0;
    private int threshold;

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public MyHashMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: "
                                               + initialCapacity);
        }
        int capacity = DEFAULT_INITIAL_CAPACITY;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }
        table = (Node<K,V>[]) new Node[capacity];
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
    }

    static class Node<K, V> {
        int hash;
        K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }

        public Node(Node<K, V> node) {
            hash = node.hash;
            key = node.key;
            value = node.value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node node = (Node) o;
            return Objects.equals(key, node.key)
                   && Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    @Override
    public void put(K key, V value) {
        putNode(new Node<>(hash(key), key, value));
    }

    @Override
    public V getValue(K key) {
        int bucket = (table.length - 1) & hash(key);
        for (Node<K, V> node = table[bucket]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        int hash;
        return (key == null) ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    private void resize() {
        size = 0;
        int newLength = table.length << 1;
        Node<K,V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[newLength];
        threshold = (int) (newLength * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                putNode(new Node<>(node));
                node = node.next;
            }
        }
    }

    private void putNode(Node<K, V> newNode) {
        int bucket = (table.length - 1) & newNode.hash;
        Node<K, V> existNode = table[bucket];
        if (existNode == null) {
            table[bucket] = newNode;
        } else {
            while (existNode.next != null || Objects.equals(existNode.key, newNode.key)) {
                if (Objects.equals(existNode.key, newNode.key)) {
                    existNode.value = newNode.value;
                    return;
                }
                existNode = existNode.next;
            }
            existNode.next = newNode;
        }
        if (++size > threshold) {
            resize();
        }
    }
}
