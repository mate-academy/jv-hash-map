package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INCREASE_COEFFICIENT = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int)(INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> currentNode = new Node<>(key, value, null);
        Node<K, V> nodeFromTable = table[index];
        if (nodeFromTable == null) {
            table[index] = currentNode;
            size++;
            return;
        }
        while (nodeFromTable.next != null || Objects.equals(nodeFromTable.key, key)) {
            if (Objects.equals(nodeFromTable.key, key)) {
                nodeFromTable.value = value;
                return;
            }
            nodeFromTable = nodeFromTable.next;
        }
        nodeFromTable.next = currentNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
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

    private Node<K, V>[] resize() {
        final Node<K, V>[] oldTable = table;
        Node<K, V> currentNode;
        table = new Node[INITIAL_CAPACITY * INCREASE_COEFFICIENT];
        threshold = (int) (table.length * LOAD_FACTOR);
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            currentNode = oldTable[i];
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
        return table;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
