package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int CAPACITY = 16;
    private int capacity;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        capacity = CAPACITY;
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> current = table[index];
        Node<K, V> newNode = new Node(key, value);
        if (current == null) {
            table[index] = newNode;
            size++;
            return;
        }

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = newNode;
                size++;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> i = table[index];
        while (i != null) {
            if (Objects.equals(i.key, key)) {
                return i.value;
            }
            i = i.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size >= capacity * LOAD_FACTOR) {
            capacity *= 2;
            Node<K, V>[] newTable = new Node[capacity];
            for (Node<K, V> node : table) {
                while (node != null) {
                    Node<K, V> current = node;
                    node = node.next;
                    int index = getIndex(current.key);
                    current.next = newTable[index];
                    newTable[index] = current;
                }
            }
            table = newTable;
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private class Node<K, V> {
        private final K key;
        private final int hash;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = key == null ? 0 : key.hashCode();
        }
    }
}
