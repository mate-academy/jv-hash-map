package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int hash = (key == null) ? 0 : key.hashCode();
        int index = (table.length - 1) & hash;

        if (table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        }

        Node<K, V> current = table[index];
        while (true) {
            if (current.hash == hash && (Objects.equals(key, current.key))) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Node<>(key, value);
                size++;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public V getValue(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        int index = (table.length - 1) & hash;

        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash && (Objects.equals(key, current.key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        threshold = (int) (newCapacity * LOAD_FACTOR);

        for (Node<K, V> oldNode : table) {
            Node<K, V> current = oldNode;
            while (current != null) {
                Node<K, V> next = current.next;
                int newIndex = (newTable.length - 1) & current.hash;

                current.next = newTable[newIndex];
                newTable[newIndex] = current;

                current = next;
            }
        }
        table = newTable;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;
        private final int hash;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = (key == null) ? 0 : key.hashCode();
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
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
}
