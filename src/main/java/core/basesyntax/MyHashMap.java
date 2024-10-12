package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity = DEFAULT_CAPACITY;
    private Node<K, V>[] table = new Node[capacity];
    private int size = 0;
    private int threshold = 0;

    @Override
    public void put(K key, V value) {
        int hash = key == null ? 0 : key.hashCode();
        int index = Math.abs(hash) % capacity;
        if (index < 0) {
            index += capacity;
        }
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.hash = hash;
        newNode.next = table[index];
        table[index] = newNode;
        size++;

        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (index == -1) {
            return null;
        }
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    public void resize() {
        capacity = capacity * 2;
        Node<K, V>[] newTable = new Node[capacity];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                Node<K, V> nextNode = node.next;
                int newIndex = node.hash % capacity;
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = nextNode;
            }
        }
        table = newTable;
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    public int getIndex(K key) {
        int hash = key == null ? 0 : key.hashCode();
        int index = Math.abs(hash) % capacity;
        if (index < 0) {
            index += capacity;
        }
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return index;
            }
            node = node.next;
        }
        return -1;
    }

    class Node<K, V> {
        private int hash;
        private Node<K, V> next;
        private K key;
        private V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public int getHash() {
            return hash;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
