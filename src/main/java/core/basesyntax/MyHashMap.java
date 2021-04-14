package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INCREASE_COEFFICIENT = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

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
        Node<K, V> positionInTable = table[index];
        if (table[index] == null) {
            table[index] = currentNode;
            size++;
        } else {
            while (positionInTable.next != null || Objects.equals(positionInTable.key, key)) {
                if (Objects.equals(positionInTable.key, key)) {
                    positionInTable.value = value;
                    return;
                }
                positionInTable = positionInTable.next;
            }
            positionInTable.next = currentNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            return null;
        }
        while (currentNode.next != null || Objects.equals(currentNode.key, key)) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return currentNode.value;
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
}
