package core.basesyntax;

import java.util.Arrays;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold = Math.round(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> processingNode = table[index];
        if (processingNode == null) {
            table[index] = new Node<K, V>(key, value, null);
            size++;
        } else {
            while (processingNode.next != null) {
                if (areValuesEqual(processingNode.key, key)) {
                    processingNode.value = value;
                    return;
                }
                processingNode = processingNode.next;
            }
            if (areValuesEqual(processingNode.key, key)) {
                processingNode.value = value;
                return;
            }
            processingNode.next = new Node(key, value, null);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> processingNode = table[index];
        while (true) {
            if (processingNode == null) {
                return null;
            } else if (areValuesEqual(processingNode.key, key)) {
                return processingNode.getValue();
            } else {
                processingNode = processingNode.next;
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean areValuesEqual(K key, K current) {
        return (current != null && current.equals(key))
                || (current == null && key == null);
    }

    private void resize() {
        size = 0;
        int newCapacity = table.length * 2;
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        threshold = Math.round(newCapacity * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> kvNode : oldTable) {
            while (kvNode != null) {
                this.put(kvNode.key, kvNode.value);
                kvNode = kvNode.next;
            }
        }
    }

    @Override
    public String toString() {
        return "MyHashMap{"
                + ", table = "
                + Arrays.toString(table)
                + ", size = " + size
                + '}';
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }
    }

    private int getIndex(K key) {
        if (key != null) {
            return Math.abs(key.hashCode()) % table.length;
        }
        return 0;
    }
}
