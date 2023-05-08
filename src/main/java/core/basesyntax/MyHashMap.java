package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] table;
    private int tableLength;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        tableLength = table.length;
        threshold = getDefaultThreshold();
    }

    @Override
    public void put(K key, V value) {
        int index = index(key);
        resizeIfNeeded();
        Node<K, V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> previous = null;
            Node<K, V> current = table[index];
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                previous = current;
                current = current.next;
            }
            previous.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = index(key);
        Node<K, V> checkNode = table[index];
        while (checkNode != null) {
            if (Objects.equals(checkNode.key, key)) {
                return checkNode.value;
            }
            checkNode = checkNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int index(K key) {
        return (key == null ? 0 : Math.abs(key.hashCode()) % tableLength);
    }

    private Node<K, V>[] resize() {
        int newLength = tableLength * GROW_FACTOR;
        Node<K, V>[] newTable = new Node[newLength];
        tableLength = newLength;
        for (Node<K, V> node : table) {
            while (node != null) {
                int newIndex = index(node.key);
                Node<K, V> next = node.next;
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
        threshold = (int) (newLength * DEFAULT_LOAD_FACTOR);
        return newTable;
    }

    private int getDefaultThreshold() {
        return threshold = (int) (DEFAULT_LOAD_FACTOR * tableLength);
    }

    private void resizeIfNeeded() {
        if (size == threshold) {
            table = resize();
        }
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
