package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double load_factor = 0.75;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int size;

    @Override
    public void put(K key, V value) {
        if (size >= table.length * load_factor) {
            resize();
        }
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        Node<K, V> current = table[index];
        while (current.next != null || Objects.equals(current.key, key)) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        current.next = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> oldNode = table[getIndex(key)];
        for (int i = 0; i < table.length; i++) {
            while (oldNode != null) {
                if (Objects.equals(oldNode.key, key)) {
                    return oldNode.value;
                } else {
                    oldNode = oldNode.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % DEFAULT_CAPACITY);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] newSize = table;
        table = new Node[table.length * 2];
        for (Node<K, V> nodes : newSize) {
            Node<K, V> node = nodes;
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
