package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K,V>[] table;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = hash(key);
        Node<K,V> newNode = new Node<>(key,value);
        Node<K,V> currentNode = table[index];
        Node<K,V> prevNode = null;
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, newNode.key)) {
                    currentNode.value = newNode.value;
                    return;
                }
                prevNode = currentNode;
                currentNode = currentNode.next;
            }
            prevNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K,V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
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
        size = 0;
        Node<K,V> [] oldTable = table;
        table = new Node[table.length * 2];
        for (Node<K,V> bucket: oldTable) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
