package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] table;
    private Node<K, V> node;
    private int threshold;
    private int size;

    @Override
    public void put(K key, V value) {
        setInitialCapacity();
        int index = getIndex(key);
        node = table[index];
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                resize();
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int index = getIndex(key);
        node = table[index];
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

    private void setInitialCapacity() {
        if (table == null) {
            table = new Node[DEFAULT_INITIAL_CAPACITY];
            int newCapacity = table.length;
            threshold = (int) (newCapacity * LOAD_FACTOR);
        }
    }

    private void resize() {
        if (size > threshold) {
            int newCapacity = table.length * GROW_FACTOR;
            threshold = (int) (newCapacity * LOAD_FACTOR);
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * GROW_FACTOR];
            size = 0;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private class Node<K, V> {
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
