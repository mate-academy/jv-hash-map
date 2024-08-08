package core.basesyntax;

import static java.util.Objects.hash;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int MAXIMUM_CAPACITY = 1073741824;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int GROW_CAPACITY_FACTOR = 2;

    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        putVal(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        if (node == null) {
            return null;
        } else {
            return node.value;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNode(K key) {
        int index = getIndexFromHash(hash(key));

        if (table[index] != null) {
            Node<K, V> node = table[index];
            do {
                if (node.hash == hash(key) && Objects.equals(key, node.key)) {
                    return node;
                }
                node = node.next;
            } while (node != null);
        }
        return null;
    }

    private void putVal(int hash, K key, V value) {
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        int index = getIndexFromHash(hash);

        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> node = table[index];
            do {
                if (node.hash == hash && Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                if (node.next != null) {
                    node = node.next;
                }
            } while (node.next != null);
            node.next = newNode;
        }
        if (++size > threshold) {
            resize();
        }
    }

    private int getIndexFromHash(int hash) {
        return Math.abs(hash % table.length);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldTableLength = table.length;

        if (oldTableLength >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
        } else {
            int newLength = oldTableLength * GROW_CAPACITY_FACTOR;
            threshold = (int) (newLength * DEFAULT_LOAD_FACTOR);
            table = new Node[newLength];
        }
        relinkNodes(oldTable, oldTableLength);
    }

    private void relinkNodes(Node<K, V>[] oldTable, int oldTableLength) {
        for (int j = 0; j < oldTableLength; j++) {
            Node<K, V> node = oldTable[j];
            if (node != null) {
                oldTable[j] = null;
                int index = getIndexFromHash(node.hash);

                if (node.next == null) {
                    table[index] = node;
                } else {
                    while (node != null) {
                        index = getIndexFromHash(node.hash);
                        Node<K, V> nodeInsertionPos = table[index];
                        if (nodeInsertionPos == null) {
                            table[index] = node;
                        } else {
                            while (nodeInsertionPos.next != null) {
                                nodeInsertionPos = nodeInsertionPos.next;
                            }
                            nodeInsertionPos.next = node;
                        }
                        nodeInsertionPos = node;
                        node = node.next;
                        nodeInsertionPos.next = null;
                    }
                }
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private final int hash;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
