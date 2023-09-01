package core.basesyntax;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final int DEFAULT_CAPACITY = 16;
    private final double LOAD_FACTOR = 0.75;
    private final int DEFAULT_INCREASE = 2;
    private int threshold;
    private int size;
    private int capacity;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        capacity = DEFAULT_CAPACITY;
    }


    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        putAfterResize(key, value);
    }

    @Override
    public V getValue(K key) {
        int index = findIndex(key);
        Node<K,V> current = table[index];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
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

    private void putAfterResize(K key, V value) {
        int index = findIndex(key);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(key, value);
            size++;
        } else {
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    break;
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value);
                    size++;
                    break;
                }
                node = node.next;
            }
        }
    }

    private void resize() {
        Node<K,V>[] oldTab = table;
        capacity = capacity * DEFAULT_INCREASE;
        Node<K,V>[] newTab = new Node[capacity];
        table = newTab;
        size = 0;
        for (Node<K,V> node: oldTab) {
            while (node != null) {
                put(node.getKey(), node.getValue());
                node = node.next;
            }
        }
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    private int findIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    public class Node<K, V> implements Map.Entry<K, V> {
        int hash;
        K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return null;
        }

        @Override
        public V getValue() {
            return null;
        }

        @Override
        public V setValue(V value) {
            return null;
        }
    }
}
