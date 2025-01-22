package core.basesyntax;

import static java.lang.Math.abs;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K,V>[] table;
    private int capacity;
    private float loadFactor;
    private int size;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.table = (Node<K,V>[]) new Node[capacity];
    }

    public static class Node<K,V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K,V> item) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = item;
        }

        public int getHash() {
            return hash;
        }

        public K getKey() {
            return key;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        Node<K, V> bucketEntry = table[index];
        // Handle null key
        if (bucketEntry == null) {
            table[index] = new Node(index, key, value, null);
            size++;
        } else {
            if (bucketEntry.hash == table[index].hash && (bucketEntry.key == key || key != null && key.equals(bucketEntry.key))) {
                bucketEntry.value = value;
                return;
            }

            while (bucketEntry.next != null) {
                if (bucketEntry.hash == table[index].hash && (bucketEntry.key == key || key != null && key.equals(bucketEntry.key))) {
                    bucketEntry.value = value;
                    return;
                }
                bucketEntry = bucketEntry.next;
            }

            bucketEntry.next = new Node(index, key, value, null);
            size++;
        }

        if (size > capacity * loadFactor) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null || table.length == 0) {
            return null;
        }
        int index = hash(key);
        Node<K, V> node = table[index];

        // Traverse the linked list at the calculated index
        while (node != null) {
            // Explicitly check for null key
            if (key == null) {
                if (node.key == null) { // Found the node with a null key
                    return node.value;
                }
            } else if (node.hash == index || key != null && key.equals(node.key)) {
                // Found the node with a non-null key
                return node.value;
            }
            node = node.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int hash(Object key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        return (h ^ (h >>> 16)) & (capacity - 1);
//        return (key == null) ? 0 : abs(key.hashCode() % 16);
//        return abs(key.hashCode() % 16);
    }

    private void resize() {
        int newCapacity = capacity * 2;
        // Double the capacity
        Node<K, V>[] newTable = new Node[newCapacity]; // Create a new table
        for (int i = 0; i < capacity * loadFactor; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                Node<K, V> next = node.next; // Save reference to the next node
                int newIndex = abs(node.key == null ? 0 : (node.hash ^ (node.hash >>> 16)) & (newCapacity - 1));
                node.next = newTable[newIndex]; // Rehash the node to the new index
                newTable[newIndex] = node; // Place the node in the new table
                node = next; // Move to the next node
            }
        }
        table = newTable;
        capacity = newCapacity;// Replace the old table with the new one
    }
}
