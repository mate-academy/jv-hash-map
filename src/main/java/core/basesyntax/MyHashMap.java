package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;

    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private double loadFactor = 0.75;
    private int size = 0;

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(hash(key), key, value, null);
        int index = Math.abs(hash(key) % table.length);
        Node<K, V> pointer = table[index];
        if (table[index] == null) {
            table[index] = node;
            size++;
            if (size > table.length * loadFactor) {
                resize(table);
            }
        } else {
            while (pointer.next != null) {
                if (Objects.equals(pointer.key, key)) {
                    pointer.value = value;
                    return;
                }
                pointer = pointer.next;
            }
            if (Objects.equals(pointer.key, key)) {
                pointer.value = value;
                return;
            }
            pointer.next = node;
            size++;
            if (size > table.length * loadFactor) {
                resize(table);
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(hash(key) % table.length);
        Node<K, V> pointer = table[index];
        while (pointer != null) {
            if (Objects.equals(pointer.key, key)) {
                return pointer.value;
            }
            pointer = pointer.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putOll(Node<K, V>[] table) {
        for (Node<K, V> backet : table) {
            while (backet != null) {
                put(backet.key, backet.value);
                backet = backet.next;
            }
        }
    }

    private void resize(Node<K, V>[] table) {
        int newCapacity = table.length * 2;
        Node<K, V>[] arrayGrow = (Node<K, V>[]) new Node[newCapacity];
        Node<K, V>[] oldTable = this.table;
        this.table = arrayGrow;
        size = 0;
        putOll(oldTable);
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash,K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
