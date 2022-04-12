package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTORY = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * DEFAULT_LOAD_FACTORY) {
            resize();
        }
        int keyHash = getHash(key);
        Node<K, V> newNode = table[keyHash];
        if (table[keyHash] == null) {
            table[keyHash] = new Node<>(key, value, null);
        }
        while (newNode != null) {
            if (Objects.equals(newNode.key, key)) {
                newNode.value = value;
                return;
            }
            if (newNode.next == null) {
                newNode.next = new Node<>(key, value, null);
                break;
            }
            newNode = newNode.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int keyHash = getHash(key);
        Node<K, V> current = table[keyHash];
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

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        int capacity = table.length;
        table = new Node[capacity * 2];
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
