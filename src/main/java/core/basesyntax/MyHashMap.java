package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;

    private int capacity;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        this.table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }

        int keyHash = key == null ? 0 : key.hashCode();
        int indexToPut = Math.abs(keyHash % capacity);

        Node<K, V> currentNode = getNode(indexToPut, key);
        Node<K, V> nodeToAdd = new Node<>(keyHash, key, value, null);

        if (currentNode == null) {
            table[indexToPut] = nodeToAdd;
        } else if (Objects.equals(currentNode.key, key)) {
            currentNode.value = value;
            return;
        } else {
            currentNode.next = nodeToAdd;
        }

        size++;
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(key == null ? 0 : key.hashCode() % capacity);
        Node<K, V> currentNode = getNode(index, key);
        return currentNode == null ? null : currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private MyHashMap.Node<K,V> next;

        Node(int hash, K key, V value, MyHashMap.Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        capacity *= RESIZE_FACTOR;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] resizedTable = (Node<K, V>[]) new Node[capacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                int newIndex = Math.abs(node.hash % capacity);
                Node<K, V> newNode = new Node<>(node.hash, node.key, node.value, null);

                if (resizedTable[newIndex] == null) {
                    resizedTable[newIndex] = newNode;
                } else {
                    Node<K, V> current = resizedTable[newIndex];
                    while (current.next != null) {
                        current = current.next;
                    }
                    current.next = newNode;
                }

                node = node.next;
            }
        }

        table = resizedTable;
    }

    private Node<K, V> getNode(int index, K key) {
        Node<K, V> currentNode = table[index];

        if (currentNode == null) {
            return null;
        }

        while (currentNode.next != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }

        return currentNode;
    }
}
