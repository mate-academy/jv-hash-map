package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> existingNode = getHeadNodeInPosition(key);

        if (existingNode != null) {
            Node<K, V> current = existingNode;
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
        }

        int threshold = (int)(table.length * LOAD_FACTOR);
        if (size == threshold) {
            resize();
            existingNode = getHeadNodeInPosition(key);
        }
        Node<K, V> node = new Node<K, V>(key, value);
        if (existingNode != null) {
            while (existingNode.next != null) {
                existingNode = existingNode.next;
            }
            existingNode.next = node;
        } else {
            table[calculateIndex(key)] = node;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = getHeadNodeInPosition(key);

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

    private void resize() {
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

    private Node<K, V> getHeadNodeInPosition(K key) {
        int index = calculateIndex(key);

        return table[index];
    }

    private int calculateIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.hash = key == null ? 0 : Math.abs(key.hashCode());
            this.key = key;
            this.value = value;
        }
    }
}
