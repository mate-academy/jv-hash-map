package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V> [] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        Node<K, V> node = new Node<>(key, value, null);
        int indexOfTable = hash(key);
        if (size == threshold) {
            resize();
        }
        if (table[indexOfTable] == null) {
            table[indexOfTable] = node;
            size++;
        } else {
            Node<K, V> newNode = table[indexOfTable];
            while (newNode != null) {
                if (Objects.equals(node.key, newNode.key)) {
                    newNode.value = value;
                    return;
                }
                if (newNode.next == null) {
                    newNode.next = node;
                    size++;
                    return;
                }
                newNode = newNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int indexOfTable = hash(key);
        Node<K, V> node = table[indexOfTable];
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
        table = new Node[table.length * 2];
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

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
