package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_RESIZE_MULTIPLIER = 2;
    private int size;
    private Node<K,V>[] table;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        size = 0;
        table = new Node[DEFAULT_CAPACITY];
    }

    @SuppressWarnings("unchecked")
    private void resizeIfNeeded() {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            Node<K,V>[] oldTable = table;
            table = new Node[table.length * DEFAULT_RESIZE_MULTIPLIER];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    size--;
                    node = node.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;
        } else {
            Node<K, V> node = table[index];
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                } else if (node.next == null) {
                    node.next = new Node<>(key, value);
                    size++;
                }
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
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

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
