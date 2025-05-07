package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        int bucketIndex = getHash(key);
        Node<K, V> nodeToPut = new Node<>(bucketIndex, key, value, null);
        Node<K, V> currentNode = table[bucketIndex];
        if (currentNode == null) {
            table[bucketIndex] = nodeToPut;
        } else {
            while (currentNode != null) {
                if (currentNode.key == key
                        || (currentNode.hash == bucketIndex
                        && Objects.equals(key, currentNode.key))) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = nodeToPut;
                    break;
                }
                currentNode = currentNode.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getHash(key);
        Node<K, V> currentNode = table[bucketIndex];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
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

    private int getHash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldNodes = table;
        int newSize = table.length * 2;
        table = new Node[newSize];
        for (Node<K, V> node : oldNodes) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
