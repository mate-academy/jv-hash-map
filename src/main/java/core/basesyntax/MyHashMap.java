package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] myTable;
    private int size;

    public MyHashMap() {
        myTable = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == myTable.length * DEFAULT_LOAD_FACTOR) {
            grow();
        }
        Node<K,V> newNode = myTable[getIndex(key)];
        if (newNode == null) {
            myTable[getIndex(key)] = new Node(key, value, null);
            size++;
        } else {
            while (newNode != null) {
                if (Objects.equals(key, newNode.key)) {
                    newNode.value = value;
                    return;
                }
                if (newNode.next == null) {
                    Node<K,V> temp = new Node<>(key, value, null);
                    newNode.next = temp;
                    size++;
                    return;
                }
                newNode = newNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> tempNode = myTable[getIndex(key)];
        while (tempNode != null) {
            if (Objects.equals(tempNode.key, key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % myTable.length);
    }

    private void grow() {
        size = 0;
        Node<K,V>[] tempTable = myTable;
        myTable = new Node[tempTable.length * 2];
        for (Node<K,V> temp : tempTable) {
            while (temp != null) {
                put(temp.key, temp.value);
                temp = temp.next;
            }
        }
    }
}
