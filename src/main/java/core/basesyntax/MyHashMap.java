package core.basesyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private int threshold = (int) (LOAD_FACTOR * DEFAULT_CAPACITY);
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        size++;
        int index = getIndex(key);
        Node<K, V> node = table[index];
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            return;
        }
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                size--;
                return;
            }
            if (node.next == null) {
                break;
            }
            node = node.next;
        }
        node.next = new Node<>(key, value, null);

    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public List<V> getValues() {
        Node<K, V>[] nodes = getAllNodes();
        List<V> values = new ArrayList<>();
        for (Node<K, V> node : nodes) {
            values.add(node.value);
        }
        return values;
    }

    @Override
    public List<K> getKeys() {
        Node<K, V>[] nodes = getAllNodes();
        List<K> keys = new ArrayList<>();
        for (Node<K, V> node : nodes) {
            keys.add(node.key);
        }
        return keys;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] getAllNodes() {
        Node<K, V>[] nodes = new Node[size];
        int index = 0;
        for (Node<K, V> node : table) {
            if (node != null) {
                nodes[index] = node;
                index++;
                while (nodes[index - 1].next != null) {
                    nodes[index] = nodes[index - 1].next;
                    index++;
                }
            }
        }
        return nodes;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        final Node<K, V>[] nodes = getAllNodes();
        table = new Node[table.length << 1];
        threshold = (int) (table.length * LOAD_FACTOR);
        size = 0;
        for (Node<K, V> node : nodes) {
            if (node == null) {
                break;
            }
            put(node.key, node.value);
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
