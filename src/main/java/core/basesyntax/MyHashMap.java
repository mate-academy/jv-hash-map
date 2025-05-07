package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getBucketIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = newNode;
            size++;
        } else {
            putInLinkedList(currentNode, key, value);
        }

    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> currentNode : table) {
            while (currentNode != null) {
                if (currentNode.key == key
                        || currentNode.key != null && currentNode.key.equals(key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : (key.hashCode() >>> 1) % table.length;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] newTable = table;
        table = new Node[newTable.length << 1];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> node : newTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void putInLinkedList(Node<K, V> currentNode, K key, V value) {
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value, null);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    private static class Node<K, V> {
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
