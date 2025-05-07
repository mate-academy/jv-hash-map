package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_INCREASE = 2;
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int bucket = getBucket(key);
        Node<K, V> current = table[bucket];
        if (current == null) {
            table[bucket] = new Node<>(key, value);
            size++;
        } else {
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                } else if (current.next == null) {
                    current.next = new Node<>(key, value);
                    size++;
                    return;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int bucket = getBucket(key);
        Node<K, V> node = table[bucket];
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

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * CAPACITY_INCREASE];
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

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
