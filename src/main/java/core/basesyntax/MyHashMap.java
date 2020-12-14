package core.basesyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final short GROWTH_FACTOR = 2;
    private int threshold = 12; //initial threshold value
    private int size;
    private int capacity;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        putToTable(key, value, table);
    }

    public void putAll(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public boolean containsKey(K key) {
        Node<K, V> node = table[getHash(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    public boolean containsValue(V value) {
        for (Node<K, V> node : table) {
            while (node != null) {
                if (Objects.equals(value, node.value)) {
                    return true;
                }
                node = node.next;
            }
        }
        return false;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getHash(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    /*
    wasn't tested
     */
    public V remove(K key) {
        Node<K, V> node = table[getHash(key)];
        if (node == null) {
            return null;
        }
        if (Objects.equals(key, node.key)) {
            table[getHash(key)] = node.next;
            return node.value;
        }
        Node<K, V> next = node.next;
        while (next != null) {
            if (Objects.equals(next.key, key)) {
                node.next = next.next;
                return next.value;
            }
            node = node.next;
            next = next.next;
        }
        return null;
    }

    /*
    removes false if key wasn't found in the HashMap
    true if such a key was found, thus removed
     */
    public boolean remove(K key, V value) {
        if (remove(key) == null) {
            return false;
        }
        return true;
    }

    @Override
    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public List<V> getValues() {
        List<V> list = new ArrayList<>();
        if (size == 0) {
            return list;
        }
        for (Node<K, V> node : table) {
            while (node != null) {
                list.add(node.value);
                node = node.next;
            }
        }
        return list;
    }

    private void resize() {
        capacity = capacity * GROWTH_FACTOR;
        threshold = threshold * GROWTH_FACTOR;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> node : table) { //Transfer
            while (node != null) {
                putToTable(node.key, node.value, newTable);
                node = node.next;
                size--;
            }
        }
        table = newTable;
    }

    private void putToTable(K key, V value, Node<K, V>[] table) {
        Node<K, V> node = new Node<>(getHash(key), key, value, null);
        if (table[node.hash] == null) {
            table[node.hash] = node;
            size++;
            return;
        }
        Node<K, V> tableNode = table[node.hash];
        while (tableNode.next != null
                || Objects.equals(tableNode.key, node.key)) {
            if (Objects.equals(tableNode.key, node.key)) {
                tableNode.value = node.value;
                return;
            }
            tableNode = tableNode.next;
        }
        tableNode.next = node;
        size++;
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;
        private int hash;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.next = next;
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }
}
