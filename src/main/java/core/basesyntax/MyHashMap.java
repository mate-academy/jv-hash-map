package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int INITIAL_CAPACITY = 16;
    public static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * LOAD_FACTOR) {
            table = resize(table);
        }
        if (table[hash(key)] == null) {
            Node currentNode = new Node(hash(key), key, value, null);
            table[hash(key)] = currentNode;
        } else {
            Node<K, V> lastNode = table[hash(key)];
            if (isKeyEquals(lastNode, key, value)) {
                return;
            }
            while (lastNode.next != null) {
                if (isKeyEquals(lastNode, key, value)) {
                    return;
                }
                lastNode = lastNode.next;
            }
            if (isKeyEquals(lastNode, key, value)) {
                return;
            }
            Node<K, V> currentNode = new Node<>(hash(key), key, value, null);
            lastNode.next = currentNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        Node<K, V> currentNode = table[hash(key)];
        while (currentNode.key != key
                || (currentNode.key != null && !currentNode.key.equals(key))) {
            if (currentNode.key != null && currentNode.key.equals(key)) {
                return (V) currentNode.value;
            }
            if (currentNode.next == null) {
                return null;
            }
            currentNode = currentNode.next;
        }
        return (V) currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        int result = key == null ? 0 : key.hashCode() % table.length;
        return result < 0 ? result * -1 : result;
    }

    private int resizeHash(K key) {
        int result = key == null ? 0 : key.hashCode() % (table.length * 2);
        return result < 0 ? result * -1 : result;
    }

    private Node<K, V>[] resize(Node<K, V>[] table) {
        Node<K, V>[] result = new Node[table.length * 2];
        for (Node<K, V> node : table) {
            if (node != null) {
                result[resizeHash(node.key)] = node;
            }
        }
        return result;
    }

    private boolean isKeyEquals(Node<K, V> node, K key, V value) {
        if (Objects.equals(node.key, key)) {
            node.value = value;
            return true;
        }
        return false;
    }
}
