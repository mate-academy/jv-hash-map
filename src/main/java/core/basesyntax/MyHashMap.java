package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_TABLE_INCREASING = 2;
    private int threshold;
    private int capacity;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int index = calculateIndex(key);
        Node<K,V> possibleNode = table[index];
        if (possibleNode == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        }
        while (possibleNode.next != null || Objects.equals(possibleNode.key, key)) {
            if (Objects.equals(possibleNode.key, key)) {
                possibleNode.value = value;
                return;
            }
            possibleNode = possibleNode.next;
        }
        possibleNode.next = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> nodeToSearch = table[calculateIndex(key)];
        if (nodeToSearch == null) {
            return null;
        }
        while (!Objects.equals(nodeToSearch.key, key)) {
            if (nodeToSearch.next == null) {
                return null;
            }
            nodeToSearch = nodeToSearch.next;
        }
        return nodeToSearch.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeIfNeeded() {
        if (size >= threshold) {
            capacity *= DEFAULT_TABLE_INCREASING;
            threshold *= DEFAULT_TABLE_INCREASING;
            Node<K, V>[] oldTable = table;
            table = new Node[capacity];
            size = 0;
            for (Node<K, V> oldNode : oldTable) {
                while (oldNode != null) {
                    put(oldNode.key, oldNode.value);
                    oldNode = oldNode.next;
                }
            }
        }
    }

    private int calculateIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
