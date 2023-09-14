package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        checkLoad(table);
        int hash = hash(key);
        Node<K, V> newNode = new Node<>(hash, key, value, null);

        if (key == null) {
            newNode.hash = 0;
        }

        if (table[hash] == null) {
            table[hash] = newNode;
        } else {
            Node<K, V> currentNode = table[hash];
            while (currentNode != null) {
                if (currentNode.key == null ? key == null : currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    break;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node<K, V> currentNode = table[hash];
        while (currentNode != null) {
            if (currentNode.key == null ? key == null : currentNode.key.equals(key)) {
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

    private void resize(Node<K, V>[] array) {
        Node<K, V>[] newTable = new Node[array.length * 2];

        for (Node<K, V> element : table) {
            while (element != null) {
                Node<K, V> next = element.next;
                int newHash = hash(element.key, newTable.length);
                element.next = newTable[newHash];
                newTable[newHash] = element;
                element = next;
            }
        }
        table = newTable;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private int hash(K key, int length) {
        return key == null ? 0 : Math.abs(key.hashCode()) % length;
    }

    private static class Node<K, V> {
        private int hash;
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

    private void checkLoad(Node<K, V>[] array) {
        if (size >= array.length * LOAD_FACTOR) {
            resize(array);
        }
    }
}
