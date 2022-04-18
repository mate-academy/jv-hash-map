package core.basesyntax;

import java.util.HashMap;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final int DEFAULT_INITIAL_CAPACITY = 16;
    private final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private int capacity;
    private Node<K, V>[] table;

    public Node<K, V>[] resize() {
        Node<K, V>[] oldTable = table;
        if (oldTable == null) {
            capacity = DEFAULT_INITIAL_CAPACITY;
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
            return table = (Node<K, V>[]) new Node[capacity];
        } else {
            capacity = capacity * 2;
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            table = (Node<K, V>[]) new Node[capacity];
        }
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> tempNode = oldTable[i];
            if (tempNode != null) {
                oldTable[i] = null;
                int position = tempNode.hash % capacity;
                if (tempNode.next == null) {
                    table[position] = tempNode;
                    table[position].next = null;
                } else {
                    Node<K, V> next = tempNode.next;
                    while (next != null) {
                        if (table[position] == null) {
                            table[position] = next;
                            table[position].next = null;
                            next = next.next;
                        } else {
                            table[position].next = next;
                            table[position].next.next = null;
                        }
                        next = next.next;
                    }
                }
            }
        }
        return table;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int position = 0;
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        if (key != null){
            position = Math.abs(newNode.hash % capacity);
        }
        if (table[position] == null) {
            table[position] = newNode;
        } else if (key == null || (table[position].hash == newNode.hash && key.equals(table[position].key))) {
            table[position].value = value;
            return;
        } else {
            Node<K, V> node = table[position];
            do {
                if (node.next == null) {
                    node.next = newNode;
                    break;
                }
                node = node.next;
            } while (node.next == null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        if (key == null) {
            return table[0].value;
        }
        int position = Math.abs(hash(key) % capacity);
        if (table[position].next == null && key.equals(table[position].key)) {
            return table[position].value;
        } else {
            Node<K, V> next = table[position];
            while (next != null) {
                if (key.equals(next.key)) {
                    return next.value;
                }
                next = next.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        if (key == null) {
            return 0;
        } else {
            return 1 + key.hashCode();
        }
    }

    private static class Node<K, V>{
        int hash;
        K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return (key == node.key || key.equals(node.key));
        }


    }
}
