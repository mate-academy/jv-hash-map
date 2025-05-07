package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_GROW = 2;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private double threshold = DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K,V> node = table[getIndex(key)];
        if (node == null) {
            table[getIndex(key)] = new Node<>(key, value);
            size++;
            return;
        } else {
            while (node.next != null) {
                if (Objects.equals(node.key,key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            if (Objects.equals(node.key,key)) {
                node.value = value;
                return;
            }
        }
        node.next = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = table[getIndex(key)];

        if (node == null) {
            return null;
        }
        while (node != null) {
            if (Objects.equals(node.key,key)) {
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
            size = 0;
            Node<K,V>[] newArray = new Node[table.length * DEFAULT_GROW];
            Node<K,V>[] oldArray = table;
            table = newArray;
            for (Node<K,V> node : oldArray) {
                if (node != null) {
                    put(node.key, node.value);
                    while (node.next != null) {
                        node = node.next;
                        put(node.key, node.value);
                    }
                }
            }
            threshold = table.length * DEFAULT_LOAD_FACTOR;
        }

    }

    public int getIndex(K key) {
        return Math.abs(key == null ? 0 : key.hashCode() % table.length);
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
