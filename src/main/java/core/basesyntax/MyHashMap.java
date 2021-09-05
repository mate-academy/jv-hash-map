package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int ARRAY_SIZE_MULTIPLIER = 2;
    static final int INDEX_FOR_NULL_KEY = 0;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);
    }

    @Override
    public void put(K key, V value) {
        tableRebuild();
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = newNode.hash % table.length;
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> workingNode = table[index];
            do {
                if (Objects.equals(key, workingNode.key)) {
                    workingNode.value = newNode.value;
                    return;
                }
                if (workingNode.next == null) {
                    workingNode.next = newNode;
                    break;
                }
                workingNode = workingNode.next;
            } while (workingNode != null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int indexOfKey;
        if (key == null) {
            indexOfKey = INDEX_FOR_NULL_KEY;
        } else {
            indexOfKey = Math.abs(Objects.hashCode(key) % table.length);
        }
        Node<K, V> currentNode = table[indexOfKey];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
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

    private void tableRebuild() {
        if (size < threshold) {
            return;
        }
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * ARRAY_SIZE_MULTIPLIER];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    private class Node<K, V> {
        private int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = hashCode();
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final int hashCode() {
            return key == null ? 0 : Math.abs(Objects.hashCode(key));
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                return Objects.equals(key, e.getKey())
                        && Objects.equals(value, e.getValue());
            }
            return false;
        }
    }
}
