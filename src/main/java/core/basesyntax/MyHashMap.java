package core.basesyntax;

import java.util.Objects;

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
            while (true) {
                if (areKeysEqual(processingNode.key, key)) {
                    processingNode.value = value;
                    return;
                }
                if (processingNode.next == null) {
                    break;
                }
                processingNode = processingNode.next;
            }
            processingNode.next = new Node<>(key, value, null);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> processingNode = table[index];
        while (processingNode != null) {
            if (areKeysEqual(processingNode.key, key)) {
                return processingNode.getValue();
            } else {
                processingNode = processingNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean areKeysEqual(K key, K currentKey) {
        return Objects.equals(key, currentKey);
    }

    private void resize() {
        size = 0;
        int newCapacity = table.length * 2;
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        threshold = Math.round(newCapacity * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                this.put(node.key, node.value);
                node = node.next;
            }
        }
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
