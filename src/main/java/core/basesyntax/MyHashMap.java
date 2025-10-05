package core.basesyntax;

import java.io.OptionalDataException;

public class MyHashMap<K, V> implements MyMap<K, V> {

    // 1️⃣ Pola klasy
    private Node<K, V>[] table;
    private int size;

    // 2️⃣ Wewnętrzna klasa Node
    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

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
        newNode.key = key;
        newNode.value = value;
        newNode.next = table[index];
        table[index] = newNode;
        size++;

    }

    @Override
    public V getValue(K key) {
        int index = (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
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

