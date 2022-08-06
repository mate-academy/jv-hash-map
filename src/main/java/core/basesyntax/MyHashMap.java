package core.basesyntax;

import java.lang.reflect.Array;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private class Node<K, V> {
        private int hash;
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(int hash, Node<K, V> next, K key, V value) {
            this.hash = hash;
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(getHash(key), null, key, value);
        if (table[newNode.hash] == null) {
            table[newNode.hash] = newNode;
        } else {
            setNext(newNode);
        }
        size ++;
        checkSizeOfTable();
    }

    @Override
    public V getValue(K key) {
        if (table[getHash(key)] == null) {
            return null;
        } else {
            Node<K, V> firstNode = table[getHash(key)];
            while (firstNode != null) {
                if (firstNode.key.equals(key)) {
                    return firstNode.value;
                }
                firstNode = firstNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkSizeOfTable() {
        if (size == (int) (table.length * DEFAULT_LOAD_FACTOR)) {
            grow();
        }
    }

    private void grow() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                Node<K, V> temp = oldTable[i];
                while (temp != null) {
                    put(temp.key, temp.value);
                    temp = temp.next;
                }
            }
        }
    }

    private int getHash(K key) {
        if (key == null) {
            return  0;
        }
        return Math.abs(key.hashCode() % table.length);
    }

    private void setNext(Node<K, V> node) {
        Node<K, V> prevNode = table[node.hash];
        while (prevNode.next != null) {
            prevNode = prevNode.next;
        }
        prevNode.next = node;
    }
}
