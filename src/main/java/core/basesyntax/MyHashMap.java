package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private final float loadFactor;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int) (capacity * loadFactor);
        this.table = (Node<K, V>[]) new Node[capacity];
        this.size = 0;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        Node<K, V> head = table[index];

        // check existing key
        for (Node<K, V> node = head; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                node.value = value; // update
                return;
            }
        }

        // insert new node
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = head;
        table[index] = newNode;
        size++;

        // check resize
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity;
        oldCapacity = capacity;
        capacity = capacity * 2;
        threshold = (int) (capacity * loadFactor);

        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;

        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                put(node.key, node.value); // rehash into new table
                node = node.next;
            }
        }
    }
}
