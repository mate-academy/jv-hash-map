package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MULTIPLIER_OF_CAPACITY = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold()) {
            resize();
        }
        Node<K, V> identicalNode = findNode(key);
        if (identicalNode != null) {
            identicalNode.value = value;
        } else {
            insertNewValue(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = findNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private int threshold() {
        return (int) (table.length * LOAD_FACTOR);
    }

    private void resize() {
        int newCapacity = table.length * MULTIPLIER_OF_CAPACITY;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        Node<K, V>[] oldTable = table;
        table = newTable;
        size = 0;
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                Node<K, V> oldNode = node;
                insertNewValue(node.key, node.value);
                while (oldNode.next != null) {
                    oldNode = oldNode.next;
                    insertNewValue(oldNode.key, oldNode.value);
                }
            }
        }
    }

    private void insertNewValue(K key, V value) {
        final int currentHash = hash(key);
        final Node<K, V> newNode = new Node<>(currentHash, key, value, null);
        Node<K, V> presentNode = table[currentHash];
        if (presentNode == null) {
            table[currentHash] = newNode;
        } else {
            while (presentNode.next != null) {
                presentNode = presentNode.next;
            }
            presentNode.next = newNode;
        }
        size++;
    }

    private Node<K, V> findNode(K key) {
        Node<K, V> fitNode = table[hash(key)];
        if (size > 0 && fitNode != null) {
            if (Objects.equals(fitNode.key, key)) {
                return fitNode;
            }
            while ((fitNode = fitNode.next) != null) {
                if (Objects.equals(fitNode.key, key)) {
                    return fitNode;
                }
            }
        }
        return null;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
