package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int INCREASE_RATE = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[INITIAL_CAPACITY];
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        int index = hash(key) % table.length;
        Node<K, V> current = table[index];
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
        } else {
            while (current.key != key && !current.key.equals(key) && current.next != null) {
                current = current.next;
            }
            if (current.key == key || current.key.equals(key)) {
                current.value = value;
                return;
            }
            current.next = new Node<>(key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key) % table.length;
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key == key || key != null && key.equals(current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        int hash;
        return key == null ? 0 : Math.abs(Objects.hash(key));
    }

    private void resize() {
        Node<K, V>[] dataFromTable = table;
        table = new Node[table.length * INCREASE_RATE];
        size = 0;
        for (Node<K, V> data : dataFromTable) {
            while (data != null) {
                put(data.key, data.value);
                data = data.next;
            }
        }
    }
}
