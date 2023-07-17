package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int capacity;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = INITIAL_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        ensureCapacity();
        int arrayIndex = getArrayIndex(key);
        Node<K, V> indexNode = table[arrayIndex];
        if (indexNode != null) {
            Node<K, V> lastNode = null;
            while (indexNode != null) {
                if (Objects.equals(indexNode.key, key)) {
                    indexNode.value = value;
                    return;
                }
                lastNode = indexNode;
                indexNode = indexNode.next;
            }
            lastNode.next = new Node<>(key, value);
        } else {
            table[arrayIndex] = new Node<>(key, value);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> tableNode = table[getArrayIndex(key)];
        while (tableNode != null) {
            if (Objects.equals(tableNode.key, key)) {
                return tableNode.value;
            }
            tableNode = tableNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void ensureCapacity() {
        if (size < threshold) {
            return;
        }
        capacity *= 2;
        threshold *= 2;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                put(node.key, node.value);
                while (node.next != null) {
                    node = node.next;
                    put(node.key, node.value);
                }
            }
        }
    }

    private int getArrayIndex(K key) {
        return ((key == null) ? 0 :
                (key.hashCode() >= 0) ? key.hashCode() : key.hashCode() * -1) % capacity;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
