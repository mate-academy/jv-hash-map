package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTORY = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int capacity;
    private int size;

    @Override
    public void put(K key, V value) {
        if (table == null) {
            table = new Node[DEFAULT_INITIAL_CAPACITY];
            capacity = DEFAULT_INITIAL_CAPACITY;
        }
        if (size > capacity * DEFAULT_LOAD_FACTORY) {
            resize();
        }
        int keyHash = getHash(key);
        if (table[keyHash] == null) {
            table[keyHash] = new Node<>(key, value, null);
        } else {
            int checking = 0;
            Node<K, V> newNode = table[keyHash];
            while (newNode.next != null) {
                if (Objects.equals(newNode.key, key)) {
                    newNode.value = value;
                    checking++;
                    size--;
                }
                newNode = newNode.next;
            }
            if (checking != 1) {
                if (Objects.equals(newNode.key, key)) {
                    newNode.value = value;
                    size--;
                }
                newNode.next = new Node<>(key, value, null);
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        for (Node<K, V> node : table) {
            Node<K, V> current = node;
            while (current != null) {
                if (Objects.equals(current.key, key)) {
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

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        table = new Node[capacity * 2];
        capacity *= 2;
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
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
    }
}
