package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private int treshold;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        treshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        size = 0;
        table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
    }

    private class Node<K,V> {
        private int hashCode;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K,V> next) {
            this.hashCode = (key != null) ? key.hashCode() : 0;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size > treshold) {
            resize();
        }
        int index = indexFor(key);
        Node<K,V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            } else if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value, null);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> currentNode = table[indexFor(key)];
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

    private void resize() {
        Node<K,V>[] newTable = (Node<K,V>[]) new Node[table.length * 2];
        Node<K,V>[] oldTable = table;
        table = newTable;
        size = 0;
        transfer(oldTable);
        treshold = (int) (table.length * LOAD_FACTOR);
    }

    private void transfer(Node<K,V>[] source) {
        for (int i = 0; i < source.length; i++) {
            if (source[i] == null) {
                continue;
            }
            Node<K,V> currentNode = source[i];
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private int indexFor(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
