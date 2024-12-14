package core.basesyntax;

import java.util.*;
import java.util.function.Consumer;

public class MyHashMap<K, V> implements MyMap<K, V> {

    transient Node<K, V>[] table;
    transient int size;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        // Check if we need to resize the table
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize(table); // Resize the table when the threshold is exceeded
        }

        int hash = hash(key);
        int index = indexFor(hash, table.length);

        // Look for an existing key
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
                // Key found, update value
                node.value = value;
                return;
            }
            node = node.next;
        }

        // Key not found, add a new node
        addNode(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        Node<K, V> node = table[index];
        while (node != null) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }

        return null; // Key not found
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addNode(int hash, K key, V value, int index) {
        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        size++;
    }

    private static int indexFor(int hash, int length) {
        return hash & (length - 1); // Use the low-order bits to find the index
    }

    // Resize the table to twice its current capacity
    private void resize(Node<K, V>[] oldTable) {
        // Double the capacity
        int newCapacity = oldTable.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];

        // Rehash all nodes from the old table and move them to the new table
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                // Calculate the index for the new table
                int newIndex = indexFor(node.hash, newCapacity);

                // Link current node to the new table at the new index
                Node<K, V> nextNode = node.next;  // Save the next node
                node.next = newTable[newIndex];    // Link current node to the new bucket
                newTable[newIndex] = node;        // Place node in the new table

                node = nextNode;  // Move to the next node in the chain
            }
        }

        // Update the table reference to the new table
        table = newTable;
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public String toString() {
            return key + "=" + value;
        }

        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry<?, ?>) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                return Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
            }
            return false;
        }
    }
}
