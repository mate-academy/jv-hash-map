package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTORY = 0.75f;
    private static final int INCREASE_COEFFICIENT = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;
    private int capacity;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTORY);
    }

    public static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            threshold = threshold * INCREASE_COEFFICIENT;
            capacity = table.length * INCREASE_COEFFICIENT;

            table = new Node[capacity];
            Node<K, V>[] oldTable = table;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
        Node<K, V> node = new Node<>(key, value, null);
        Node<K, V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = node;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        table[getIndex(key)] = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if ((node.key != null && node.key.equals(key)) || node.key == key) {
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
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }
}
