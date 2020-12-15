package core.basesyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * LOAD_FACTOR);
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
        Node<K, V> node = table[getIndex(key, table.length)];
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
        Node<K, V> node = table[getIndex(key, table.length)];
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
        Node<K, V> node = table[getIndex(key, table.length)];
        if (node == null) {
            return null;
        }
        if (Objects.equals(key, node.key)) {
            table[getIndex(key, table.length)] = node.next;
            size--;
            return node.value;
        }
        Node<K, V> next = node.next;
        while (next != null) {
            if (Objects.equals(next.key, key)) {
                node.next = next.next;
                size--;
                return next.value;
            }
            node = node.next;
            next = next.next;
        }
        return null;
    }

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
        int newCapacity = table.length * 2;
        threshold = (int) (threshold * LOAD_FACTOR);
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                putToTable(node.key, node.value, newTable);
                node = node.next;
                size--;
            }
        }
        table = newTable;
    }

    private void putToTable(K key, V value, Node<K, V>[] table) {
        Node<K, V> node = new Node<>(getIndex(key, table.length), key, value, null);
        if (table[node.index] == null) {
            table[node.index] = node;
            size++;
            return;
        }
        Node<K, V> tableNode = table[node.index];
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

    private int getIndex(K key, int tableCapacity) {
        return key == null ? 0 : Math.abs(key.hashCode() % tableCapacity);
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;
        private int index;

        public Node(int index, K key, V value, Node<K, V> next) {
            this.next = next;
            this.key = key;
            this.value = value;
            this.index = index;
        }
    }
}
