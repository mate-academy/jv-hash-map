package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_CAPACITY = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(V value, K key, Node<K, V> next) {
            this.value = value;
            this.key = key;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            reSize();
        }
        int cell = hash(key);
        Node<K, V> newNode = new Node<K, V>(value, key, null);
        if (table[cell] == null) {
            table[cell] = newNode;
            size++;
        } else {
            Node<K,V> tempNode = table[cell];
            while (tempNode != null) {
                if (Objects.equals(newNode.key, tempNode.key)) {
                    tempNode.value = newNode.value;
                    return;
                }
                if (tempNode.next == null) {
                    tempNode.next = newNode;
                    size++;
                    return;
                }
                tempNode = tempNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int cell = hash(key);
        Node<K, V> tempNode = table[cell];
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

    private void reSize() {
        Node<K, V>[] oldArray = table;
        table = (Node<K, V>[]) new Node[oldArray.length * GROW_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> tempNode : oldArray) {
            while (tempNode != null) {
                put(tempNode.key, tempNode.value);
                size--;
                tempNode = tempNode.next;
            }
        }
    }

    private int hash(Object key) {
        int cell;
        return (key == null) ? 0 : (Math.abs(cell = key.hashCode() % table.length));
    }
}
