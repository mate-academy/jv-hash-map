package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;
    private V nullKeyValue;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    private int getHash(K key) {
        int hash = (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
        return hash;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            if (nullKeyValue == null) {
                size++;
            }
            nullKeyValue = value;
            return;
        }
        resize();
        int index = getHash(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.getKey(), key)) {
                current.setValue(value);
                return;
            }
            current = current.getNext();
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.setNext(table[index]);
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return nullKeyValue;
        }
        int index = getHash(key);
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

    @SuppressWarnings("unchecked")
    private void resize() {
        if (size >= table.length * LOAD_FACTOR) {
            Node<K, V>[] oldTable = table;
            table = (Node<K, V>[]) new Node[oldTable.length * 2];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    rehashPut(node.getKey(), node.getValue());
                    node = node.getNext();
                }
            }
        }
    }

    private void rehashPut(K key, V value) {
        int index = getHash(key);
        Node<K, V> newNode = new Node<>(key, value);
        newNode.setNext(table[index]);
        table[index] = newNode;
    }
}
