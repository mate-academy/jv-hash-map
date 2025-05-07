package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MULTIPLICATION_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        addNode(key, value);
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = getBucketIndex(hash);
        Node<K, V> currentNode = table[index];
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

    private void checkSize() {
        if (size >= (int) (table.length * DEFAULT_LOAD_FACTOR)) {
            Node<K, V>[] copiedTable = table;
            table = new Node[copiedTable.length * MULTIPLICATION_FACTOR];
            for (Node<K, V> node : copiedTable) {
                while (node != null) {
                    addNode(node.key, node.value);
                    size--;
                    node = node.next;
                }
            }
        }
    }

    private void addNode(K key, V value) {
        int hash = hash(key);
        int index = getBucketIndex(hash);
        Node<K, V> node = new Node<>(hash, key, value, null);
        if (table[index] == null) {
            table[index] = node;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode.next != null || Objects.equals(currentNode.key, key)) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }

                currentNode = currentNode.next;
            }
            currentNode.next = node;
        }
        size++;
    }

    private int getBucketIndex(int hash) {
        return hash % table.length;
    }

    private int hash(K key) {
        return Math.abs(key == null ? 0 : key.hashCode());
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private int hash;
        private K key;
        private V value;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
