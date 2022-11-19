package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int capacity;
    private int threshold;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.threshold = (int)(DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        size = 0;
        table = (Node<K, V>[]) new Node[capacity];
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int num = hash(key);
        Node<K,V> node = table[num];
        if (node == null) {
            table[num] = new Node<>(key, value, null);
            size++;
        } else {
            while (node.next != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            if (Objects.equals(node.key, key)) {
                node.value = value;
            } else {
                node.next = new Node<>(key, value, null);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resize() {
        if (size == threshold) {
            capacity = capacity * 2;
            size = 0;
            Node<K,V>[] newTable = table;
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            table = (Node<K,V>[])new Node[capacity];
            transfer(newTable);
        }
    }

    private void transfer(Node<K,V>[] newTable) {
        Node<K,V>[] transTable = newTable;
        for (int i = 0; i < transTable.length; i++) {
            Node<K,V> node = transTable[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
