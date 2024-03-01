package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROWTH_COEFFICIENT = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_SIZE = 0;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        addNode(key, value);
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

    private void addNode(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndex(key);
        Node<K, V> node = table[index];

        if (node == null) {
            table[index] = newNode;
        } else {
            while (true) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                } else if (node.next == null) {
                    node.next = newNode;
                    break;
                }
                node = node.next;
            }
        }

        size++;
    }

    private void checkSize() {
        if (size == (LOAD_FACTOR * table.length)) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * GROWTH_COEFFICIENT];
        size = DEFAULT_SIZE;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return (key == null)
                ? 0
                : Math.abs(key.hashCode() % table.length);
    }

    static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
