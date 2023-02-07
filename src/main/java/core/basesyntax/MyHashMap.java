package core.basesyntax;

import java.util.HashMap;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap(){
        this.threshold = 12;
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }

        int indexOfBucket = hash(key);

        if (table[indexOfBucket] == null) {
            table[indexOfBucket] = new Node(key, value);
            size++;
        }

        Node<K, V> pointer = table[indexOfBucket];

        while (pointer != null) {
            if (pointer.key == key || key != null && key.equals(pointer.key)) {
                pointer.value = value;
                return;
            }
            if (pointer.next == null) {
                pointer.next = new Node(key, value);
                size++;
                return;
            }
            pointer = pointer.next;
        }
    }

    @Override
    public V getValue(K key) {
        int indexOfBucket = hash(key);
        Node<K, V> pointer = table[indexOfBucket];

        while (pointer != null) {
            if (pointer.key == key || key != null && key.equals(pointer.key)) {
                return pointer.value;
            } else {
                pointer = pointer.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;

        threshold = (int) (DEFAULT_LOAD_FACTOR * (table.length << 1));
        table = (Node<K, V>[]) new Node[table.length << 1];

        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    public static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
