package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    private Node<K, V>[] table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];

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

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        putVal(key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[hash(key) % table.length];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : (31 * 17 + Math.abs(key.hashCode()));
    }

    private void putVal(K key, V value) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        int numberOfCurrentBucket = newNode.hash % table.length;
        Node<K, V> currentNode = table[numberOfCurrentBucket];
        if (currentNode == null) {
            table[numberOfCurrentBucket] = newNode;
            size++;
        } else if (currentNode.key == null && key == null
                || currentNode.key != null && Objects.equals(currentNode.key, key)) {
            currentNode.value = value;
        } else {
            while (currentNode.next != null) {
                currentNode = currentNode.next;
                if (currentNode.key == null && key == null
                        || currentNode.key != null && Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
            }
            currentNode.next = newNode;
            size++;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                putVal(node.key, node.value);
                while (node.next != null) {
                    putVal(node.next.key, node.next.value);
                    node = node.next;
                }
            }
        }
    }
}
