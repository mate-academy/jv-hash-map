package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private int defaultCapacityIncrease;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        defaultCapacityIncrease = table.length * 2;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        putInTable(table, key, value);
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
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

    private int calculateIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void putInTable(Node<K, V>[] table, K key, V value) {
        int index = calculateIndex(key);
        Node<K, V> current = table[index];
        Node<K, V> newNode = new Node<>(key, value, null);
        if (current == null) {
            table[index] = newNode;
        } else {
            Node<K, V> prev = current;
            while (current != null) {
                if (Objects.equals(newNode.key, current.key)) {
                    current.value = newNode.value;
                    return;
                }
                prev = current;
                current = current.next;
            }
            prev.next = newNode;
        }
        size++;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[defaultCapacityIncrease];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                putInTable(table,node.key, node.value);
                node = node.next;
            }
        }
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
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
