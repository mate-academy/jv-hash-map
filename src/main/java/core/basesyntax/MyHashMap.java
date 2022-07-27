package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INCREASE_COEFFICIENT = 2;
    private int capacity;
    private int size;
    private int threshold;

    private Node<K,V>[] table;

    static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = (key == null ? 0 : getIndex(key));
        Node<K, V> newNode = new Node<>(key, value, null);
        if (!writeValueIfKeyExists(key, value, table[index])) {
            if (++size > threshold) {
                increaseTableSize();
            }
            if (table[index] == null) {
                table[index] = newNode;
                return;
            }
            newNode.next = table[index];
            table[index] = newNode;
        }
    }

    @Override
    public V getValue(K key) {
        int index = (key == null ? 0 : getIndex(key));
        return findByKey(key, table[index]);
    }

    @Override
    public int getSize() {
        return size;
    }

    private void increaseTableSize() {
        increaseCapacity();
        increaseThreshold();
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        moveToNewTable(oldTable);
    }

    private void moveToNewTable(Node<K, V>[] oldTable) {
        size = 1;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        return;
    }

    private boolean writeValueIfKeyExists(K key, V value, Node<K, V> current) {
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                current.value = value;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private V findByKey(K key, Node<K, V> current) {
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    private void increaseThreshold() {
        threshold = threshold * INCREASE_COEFFICIENT;
    }

    private int getIndex(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    private void increaseCapacity() {
        capacity = capacity * INCREASE_COEFFICIENT;
    }
}
