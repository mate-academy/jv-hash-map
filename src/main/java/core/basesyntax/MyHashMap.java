package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > (int) (table.length * DEFAULT_LOAD_FACTOR)) {
            growCapacity();
        }
        int tableIndex = Math.abs(Objects.hashCode(key)) % table.length;
        if (table[tableIndex] == null) {
            table[tableIndex] = new Node<>(key, value);
        } else {
            Node<K, V> iterationNode = table[tableIndex];
            do {
                if (Objects.equals(iterationNode.key, key)) {
                    iterationNode.value = value;
                    return;
                }
                iterationNode = iterationNode.next;
            } while (iterationNode != null);
            iterationNode = table[tableIndex];
            while (iterationNode.next != null) {
                iterationNode = iterationNode.next;
            }
            iterationNode.next = new Node<>(key, value);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> iterationNode = table[Math.abs(Objects.hashCode(key)) % table.length];
        while (iterationNode != null) {
            if (Objects.equals(key, iterationNode.key)) {
                return iterationNode.value;
            }
            iterationNode = iterationNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void growCapacity() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private Node<K, V> next;
        private V value;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
