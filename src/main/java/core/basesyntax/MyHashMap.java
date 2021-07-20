package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int INITIAL_CAPACITY = 1 << 4;
    static final float LOAD_FACTOR = 0.75f;
    private static final int MAGNIFICATION_FACTOR = 2;
    private int capacity = INITIAL_CAPACITY;
    private Node<K, V>[] table = new Node[INITIAL_CAPACITY];
    private int size;

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size > capacity * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> node = table[getHash(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
        table[getHash(key)] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getHash(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    int getHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private void resize() {
        capacity *= MAGNIFICATION_FACTOR;
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
