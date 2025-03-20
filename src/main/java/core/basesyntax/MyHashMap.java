package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int capacity;

    public static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = new Node[capacity];
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    public void put(K key, V value) {
        if (table == null) {
            table = new Node[capacity];
        }
        int hash = hash(key);
        int index = hash % capacity;

        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        size++;

        if (size > capacity * LOAD_FACTOR) {
            resize();
        }
    }

    public V getValue(K key) {
        int hash = hash(key);
        int index = hash % capacity;

        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> head : table) {
            while (head != null) {
                int newIndex = head.hash % newCapacity;
                Node<K, V> next = head.next;

                head.next = newTable[newIndex];
                newTable[newIndex] = head;

                head = next;
            }
        }

        capacity = newCapacity;
        table = newTable;
    }
}
