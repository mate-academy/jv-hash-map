package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
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

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getBucketIndex(key, table.length);
        Node<K, V> current = table[index];

        while (current != null) {
            if (Objects.equals(current.getKey(), key)) {
                current.setValue(value);
                return;
            }
            current = current.getNext();
        }

        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;

        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key, table.length);
        Node<K, V> current = table[index];

        while (current != null) {
            if (Objects.equals(current.getKey(), key)) {
                return current.getValue();
            }
            current = current.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndex = getBucketIndex(node.key, newCapacity);

                node.next = newTable[newIndex];
                newTable[newIndex] = node;

                node = next;
            }
        }
        table = newTable;
    }

    private int getBucketIndex(K key, int capacity) {
        return (key == null) ? 0 : (key.hashCode() & 0x7fffffff) % capacity;
    }
}
