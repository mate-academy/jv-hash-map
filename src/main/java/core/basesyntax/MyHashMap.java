package core.basesyntax;

import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    Node<K, V>[] table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    int size = 0;
    private int currentCapacity = DEFAULT_INITIAL_CAPACITY;

    public static class Node<K, V> {
        K key;
        V value;
        int hash;
        Node<K, V> next;

        public Node(K key, V value, int hash, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        int hash = key.hashCode();
        int index = hash / currentCapacity;
        Node<K, V> newNode = new Node<>(key, value, hash, null);
        if (table[index] == null) {
            table[index] = newNode;
        }
        if (table[index] != null && table[index].equals(Node.class)) {

        }


    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }
}
