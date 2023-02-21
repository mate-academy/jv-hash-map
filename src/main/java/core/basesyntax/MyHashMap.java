package core.basesyntax;

import static java.lang.Math.abs;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] table;
    private int size = DEFAULT_SIZE;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int keyHash = key == null ? 0 : abs(key.hashCode() % table.length);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[keyHash] == null) {
            table[keyHash] = newNode;
        } else {
            Node<K, V> prevNode = table[keyHash];
            if (Objects.equals(key, prevNode.key)) {
                prevNode.value = value;
                return;
            }
            while (prevNode.next != null) {
                prevNode = prevNode.next;
                if (Objects.equals(key, prevNode.key)) {
                    prevNode.value = value;
                    return;
                }
            }
            prevNode.next = newNode;
        }
        size++;
        checkSize();
    }

    @Override
    public V getValue(K key) {
        int keyHash = key == null ? 0 : abs(key.hashCode() % table.length);
        if (table[keyHash] != null) {
            Node<K, V> node = table[keyHash];
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkSize() {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            grow();
        }
    }

    private void grow() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * GROW_FACTOR];
        for (Node<K, V> kvNode : oldTable) {
            if (kvNode != null) {
                put(kvNode.key, kvNode.value);
                Node<K, V> newNode = kvNode;
                while (newNode.next != null) {
                    newNode = newNode.next;
                    put(newNode.key, newNode.value);
                }
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
