package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    // Constants for default capacity, resize coefficient, and load factor
    static final int DEFAULT_CAPACITY = 16; // aka 16
    static final int RESIZE_COEFFICIENT = 2;
    static final float LOAD_FACTOR = 0.75F;

    // Instance variables
    private int size; // Current number of elements in the map
    private int capacity = DEFAULT_CAPACITY; // Current capacity of the table

    private Node<K, V>[] table; // Array to hold the hash table

    // Constructor to initialize the hash table
    public MyHashMap() {
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);

        // Check if resizing is needed
        if (getThreshold() == size) {
            resize(); // Resize the hash table
        }

        // Calculate index based on key
        int index = getIndex(key);

        // Handle collisions
        if (table[index] != null) {
            Node<K, V> node = table[index];
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value; // Update value if key already exists
                    return;
                }
                node = node.next;
            }
            newNode.next = table[index]; // Handle collision by chaining
        }
        table[index] = newNode; // Insert new node
        size++; // Increment size
    }

    @Override
    public V getValue(K key) {
        // Calculate index based on key
        int index = getIndex(key);

        // Search for the key in the chain at the calculated index
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value; // Return value if key is found
            }
            node = node.next;
        }
        return null; // Key not found
    }

    @Override
    public int getSize() {
        return size; // Return the current size of the map
    }

    // Resize the hash table
    private void resize() {
        size = 0; // Reset size for rehashing
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * RESIZE_COEFFICIENT];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value); // Rehash the elements
                node = node.next;
            }
        }
    }

    // Calculate the threshold for resizing
    private int getThreshold() {
        return (int) (table.length * LOAD_FACTOR);
    }

    // Calculate index based on key
    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    // Nested class to represent nodes in the hash table
    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
