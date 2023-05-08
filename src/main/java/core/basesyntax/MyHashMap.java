package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;

    private int threshold = (int) (LOAD_FACTOR * INITIAL_CAPACITY);
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        putValue(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> current : table) {
            while (current != null) {
                if (Objects.equals(key, current.key)) {
                    return current.value;
                }
                current = current.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putValue(int hash, K key, V value) {
        int index = getTableIndex(table.length, hash);
        Node<K, V> current = table[index];
        Node<K, V> newNode = new Node<>(hash, key, value);
        if (current == null) {
            table[index] = newNode;
        } else {
            for (; ; ) {
                if (Objects.equals(key, current.key)) {
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
        if (++size > threshold) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity << 1;
        threshold <<= 1;
        table = (Node<K, V>[]) new Node[newCapacity];
        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> node = oldTable[i];
            if (node != null) {
                int index = getTableIndex(newCapacity, node.hash);
                oldTable[i] = null;
                if (node.next == null) {
                    table[index] = node;
                } else {
                    Node<K, V> current = node;
                    do {
                        Node<K, V> next = current.next;
                        int newIndex = getTableIndex(newCapacity, current.hash);
                        current.next = table[newIndex];
                        table[newIndex] = current;
                        current = next;
                    } while (current != null);
                }
            }
        }
    }

    private int hash(K key) {
        int hash;
        return key == null ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    private int getTableIndex(int capacity, int hash) {
        return (capacity - 1) & hash;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
