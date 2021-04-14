package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_VALUE = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V> [])new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        final Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        Node<K, V> current = table[hash(key) % table.length];
        if (current == null) {
            table[hash(key) % table.length] = newNode;
        } else if (Objects.equals(current.value, value) || Objects.equals(current.key, key)) {
            current.value = value;
            return;
        } else {
            while (current.next != null) {
                if (Objects.equals(current.next.value, value)
                        || Objects.equals(current.next.key, key)) {
                    current.next.value = value;
                    return;
                }
                current = current.next;
            }
            current.next = newNode;
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : table) {
            if (node != null) {
                do {
                    if (Objects.equals(node.key, key)) {
                        return node.value;
                    }
                    node = node.next;
                } while (node != null);
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        final int newCapacity = table.length * GROW_VALUE;
        final Node<K, V>[] oldTable = table;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        table = (Node<K,V>[])new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                do {
                    put(node.key, node.value);
                    node = node.next;
                } while (node != null);
            }
        }
    }

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }
}
