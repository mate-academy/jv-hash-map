package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INCREASE = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        Node<K, V> currentNode = table[hash];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key,key)) {
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

    private void resize() {
        threshold = (int) (table.length * DEFAULT_INCREASE * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * DEFAULT_INCREASE];
        size = 0;
        moveValues(oldTable);
    }

    private void moveValues(Node<K, V>[] oldTable) {
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                putValue(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private void putValue(K key, V value) {
        int hash = getHash(key);
        if (table[hash] == null) {
            table[hash] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> currentNode = table[hash];
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
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(hashCode() % table.length);
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
