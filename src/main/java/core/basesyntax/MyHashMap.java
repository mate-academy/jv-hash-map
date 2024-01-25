package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_RESIZE_MULTIPLIER = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        checkSize();
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> nodeInBucket = table[index];
            do {
                if (Objects.equals(nodeInBucket.key, key)) {
                    nodeInBucket.value = value;
                    return;
                }
                if (nodeInBucket.next == null) {
                    nodeInBucket.next = newNode;
                    break;
                }
                nodeInBucket = nodeInBucket.next;
            } while (nodeInBucket != null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> nodeInTable = table[index];
        while (nodeInTable != null) {
            if (Objects.equals(nodeInTable.key, key)) {
                return nodeInTable.value;
            }
            nodeInTable = nodeInTable.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        final Node<K, V>[] oldTable = table;
        Node<K, V>[] newTable = new Node[table.length * DEFAULT_RESIZE_MULTIPLIER];
        table = newTable;
        threshold = (int) (LOAD_FACTOR * newTable.length);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getIndex(K key) {
        return hash(key) % table.length;
    }

    private void checkSize() {
        if (size == threshold) {
            resize();
        }
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
