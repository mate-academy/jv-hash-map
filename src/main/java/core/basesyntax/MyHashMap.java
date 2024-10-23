package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_CAPACITY = 16;
    public static final double LOAD_FACTOR = 0.75;
    public static final int MULTIPLIER = 2;
    private int size;
    private Node<K,V>[] table = new Node[DEFAULT_CAPACITY];

    @Override
    public void put(K key, V value) {
        if (size >= (table.length * LOAD_FACTOR)) {
            resize();
        }
        int bucket = getBucket(key);
        Node<K,V> lastNode = table[bucket];
        if (lastNode == null) {
            table[bucket] = new Node<>(key, value, null);
            size++;
        } else {
            while (lastNode != null) {
                if (Objects.equals(lastNode.key, key)) {
                    lastNode.value = value;
                    return;
                } else if (lastNode.next == null) {
                    lastNode.next = new Node<>(key, value, null);
                    size++;
                    return;
                } else {
                    lastNode = lastNode.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int bucket = getBucket(key);
        Node<K,V> node = table[bucket];
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

    public void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * MULTIPLIER];
        for (Node<K, V> old : oldTable) {
            while (old != null) {
                put(old.key, old.value);
                old = old.next;
            }
        }
    }

    private int getBucket(K key) {
        return Math.abs(getHash(key) % table.length);
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private static class Node<K,V> {
        private Node<K,V> next;
        private final K key;
        private V value;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
