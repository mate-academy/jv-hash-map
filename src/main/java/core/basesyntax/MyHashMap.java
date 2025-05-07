package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int TABLE_MULTIPLIER = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int capacity;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = new Node[DEFAULT_CAPACITY];
    }

    private class Node<K, V> {

        private final K key;
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
        if (size >= threshold) {
            resize();
        }
        Node<K, V> newNodeForPut = new Node(key, value, null);
        int index = getHash(key) % table.length;
        if (table[index] == null) {
            table[index] = newNodeForPut;
            size++;
            return;
        }
        Node<K, V> currentNode = table[index];
        while (currentNode.next != null || Objects.equals(currentNode.key, key)) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        currentNode.next = newNodeForPut;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getHash(key) % table.length;
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

    private void resize() {
        size = 0;
        capacity = table.length * TABLE_MULTIPLIER;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (int i = 0; i < oldTable.length; i++) {
            while (oldTable[i] != null) {
                put(oldTable[i].key, oldTable[i].value);
                oldTable[i] = oldTable[i].next;
            }
        }
    }

    private int getHash(K key) {
        return Math.abs(key == null ? 0 : key.hashCode());
    }
}

