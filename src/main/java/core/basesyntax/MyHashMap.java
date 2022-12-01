package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            table = resize();
        }
        int index = getIndex(key, table);
        if (table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        }
        Node<K, V> nextNode = table[index];
        while (nextNode.next != null) {
            if (Objects.equals(nextNode.key, key)) {
                nextNode.value = value;
                return;
            }
            nextNode = nextNode.next;
        }
        if (Objects.equals(nextNode.key, key)) {
            nextNode.value = value;
            return;
        }
        nextNode.next = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key, table);
        Node<K, V> nextPair = table[index];
        while (nextPair != null) {
            if (Objects.equals(nextPair.key, key)) {
                return nextPair.value;
            }
            nextPair = nextPair.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        size = 0;
        Node<K, V>[] currentTable = table;
        table = new Node[currentTable.length * 2];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (int i = 0; i < currentTable.length; i++) {
            while (currentTable[i] != null) {
                put(currentTable[i].key, currentTable[i].value);
                currentTable[i] = currentTable[i].next;
            }
        }
        return table;
    }

    private int getIndex(K key, Node<K, V>[] table) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
