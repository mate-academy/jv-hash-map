package core.basesyntax;

import com.sun.jdi.Value;

public class MyHashMap<K, V> implements MyMap<K, V> {

    // 1️⃣ Pola klasy
    private Node<K, V>[] table;
    private int size;

    // 2️⃣ Wewnętrzna klasa Node
    static class Node<K, V> {
        private K key;

        public void setValue(V value) {
            this.value = value;
        }

        public V getValue() {
            return value;
        }

        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

        // opcjonalny konstruktor Node(K key, V value)
    }

    // 3️⃣ Konstruktor MyHashMap
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[4];
        size = 0;

    }

    @Override
    public void put(K key, V value) {
        int index = (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;

        Node<K, V> newNode = new Node<>();
        newNode.setKey(key);
        newNode.setValue(value);
        newNode.setNext(table[index]);
        table[index] = newNode;
        size++;

    }

    @Override
    public V getValue(K key) {
        int index = (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.getKey().equals(key)) {
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
}

