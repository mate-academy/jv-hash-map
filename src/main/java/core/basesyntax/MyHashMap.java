package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > (int) (LOAD_FACTOR * table.length)) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> newTable = new Node<>(key, value, null);
        Node<K,V> node = table[index];
        if (node == null) {
            table[index] = newTable;
        }
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newTable;
                break;
            }
            node = node.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> node = table[index];
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        for (Node<K, V> table : oldTable) {
            while (table != null) {
                put(table.key, table.value);
                table = table.next;
            }
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

