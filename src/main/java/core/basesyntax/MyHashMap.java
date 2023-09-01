package core.basesyntax;

import java.util.Arrays;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int CAPACITY_MULTIPLIER = 2;
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
        Node<K, V> bucketNode = new Node<>(key, value);
        if (size == threshold) {
            resize();
        }
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = bucketNode;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.key != null && current.key.equals(key) || current.key == key) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    bucketNode.next = table[index];
                    table[index] = bucketNode;
                }
                current = current.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key == key
                    || current.key != null && current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(table);
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldArray = table;
        table = new Node[table.length * CAPACITY_MULTIPLIER];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (Node<K, V> kvNode : oldArray) {
            while (kvNode != null) {
                put(kvNode.key, kvNode.value);
                kvNode = kvNode.next;
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
