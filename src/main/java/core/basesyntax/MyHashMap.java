package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * LOAD_FACTOR) {
            extendCapacity();
        }
        int index = getKeyIndex(key);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        }
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                break;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                break;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getKeyIndex(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    private void extendCapacity() {
        Node<K, V>[] oldTable = new Node[table.length];
        System.arraycopy(table, 0, oldTable, 0, table.length);
        table = new Node[table.length * GROW_FACTOR];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;

            }
        }
    }

    private int getKeyIndex(K key) {
        return key == null ? 0 : (key.hashCode() > 0 ? key.hashCode()
                : key.hashCode() * -1) % table.length;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
