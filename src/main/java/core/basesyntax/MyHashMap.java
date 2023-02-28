package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int ARRAY_LENGTH_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getHash(key, table.length);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> prevNode = node;
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                prevNode = node;
                node = node.next;
            }
            prevNode.next = new Node<>(key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getHash(key, table.length)];
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
        if (size >= table.length * LOAD_FACTOR) {
            size = 0;
            Node<K, V>[] tempTable = table;
            table = new Node[table.length * ARRAY_LENGTH_MULTIPLIER];
            transfer(tempTable);
        }
    }

    private void transfer(Node<K, V>[] tempTable) {
        for (Node<K, V> node : tempTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }

        }
    }

    private int getHash(K key, int length) {
        return key == null ? 0 : Math.abs(key.hashCode() % length);
    }

    private class Node<K, V> {
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
