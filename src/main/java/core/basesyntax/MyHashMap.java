package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int cell = hash(key);
        Node<K, V> newNode = new Node<K, V>(value, key);
        if (table[cell] == null) {
            table[cell] = newNode;
            size++;
        } else {
            putInSameCell(cell, newNode);
        }
        if (size == threshold) {
            reSize();
        }
    }

    @Override
    public V getValue(K key) {
        int cell = hash(key);
        if (table[cell] == null) {
            return null;
        }
        Node<K, V> tempNode = table[cell];
        while (tempNode != null) {
            if (Objects.equals(tempNode.key, key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return table[cell].value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void reSize() {
        Node<K, V>[] oldArray = table;
        table = (Node<K, V>[]) new Node[oldArray.length * 2];
        for (int i = 0; i < oldArray.length; i++) {
            if (oldArray[i] != null) {
                Node<K, V> tempNode = oldArray[i];
                while (tempNode != null) {
                    put(tempNode.key, tempNode.value);
                    size--;
                    tempNode = tempNode.next;
                }
            }
        }
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    private int hash(Object key) {
        int cell;
        return (key == null) ? 0 : (Math.abs(cell = key.hashCode() % table.length));
    }

    private void putInSameCell(int cell, Node<K, V> node) {
        Node<K, V> tempNode = table[cell];
        while (tempNode != null) {
            if (Objects.equals(node.key, tempNode.key)) {
                tempNode.value = node.value;
                return;
            } else {
                if (tempNode.next == null) {
                    tempNode.next = node;
                    size++;
                    return;
                }
            }
            tempNode = tempNode.next;
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(V value, K key) {
            this.value = value;
            this.key = key;
            this.next = null;
        }
    }
}
