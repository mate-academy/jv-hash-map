package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K,V>[] table;
    private int size = 0;
    private int threshold;

    public MyHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public MyHashMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: "
                                               + initialCapacity);
        }
        int capacity = DEFAULT_CAPACITY;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }
        table = (Node<K,V>[]) new Node[capacity];
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        putNode(new Node<>(key, value));
    }

    @Override
    public V getValue(K key) {
        int bucket = indexFor(key);
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

    public V remove(K key) {
        int bucket = indexFor(key);
        Node<K, V> node = table[bucket];
        if (node != null) {
            if (Objects.equals(key, node.key)) {
                table[bucket] = node.next;
                size--;
                return node.value;
            }
            for (; node.next != null; node = node.next) {
                if (Objects.equals(key, node.next.key)) {
                    V value = node.next.value;
                    node.next = node.next.next;
                    size--;
                    return value;
                }
            }
        }
        return null;
    }

    private int indexFor(K key) {
        return (table.length - 1) & hash(key);
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
                putNode(new Node<>(node.key, node.value));
                node = node.next;
            }
        }
    }

    private void putNode(Node<K, V> newNode) {
        int bucket = (table.length - 1) & hash(newNode.key);
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
