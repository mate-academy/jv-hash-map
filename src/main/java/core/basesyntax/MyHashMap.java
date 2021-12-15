package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPACITY = 16;
    private int size;
    private Node<K, V>[] table = new Node[INITIAL_CAPACITY];

    public int findPosition(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * LOAD_FACTOR) {
            resize();
        }
        int index = findPosition(key);
        Node<K, V> currentNode = table[index];
        if (table[index] == null) {
            Node<K, V> node = new Node<>(key, value, null);
            table[index] = node;
            size++;
            return;
        }
        if (Objects.equals(key, currentNode.key)) {
            currentNode.value = value;
            return;
        }
        while (currentNode.next != null || Objects.equals(key, currentNode.key)) {
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        currentNode.next = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = findPosition(key);
        Node<K, V> current = table[index];
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

    private void resize() {
        size = 0;
        Node<K, V>[] tempTable = table;
        table = new Node[table.length * 2];
        for (Node<K, V> resizeTable : tempTable) {
            while (resizeTable != null) {
                put(resizeTable.key, resizeTable.value);
                resizeTable = resizeTable.next;
            }
        }
    }

    private class Node<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            return value;
        }
    }
}



