package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int SCALING_CONSTANT = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    public class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        if (table[index] != null) {
            Node<K, V> newNode = table[index];
            while (newNode != null) {
                if (Objects.equals(newNode.key, key)) {
                    newNode.value = value;
                    return;
                } else if (newNode.next == null) {
                    break;
                }
                newNode = newNode.next;
            }
            if (newNode != null) {
                newNode.next = new Node<>(key, value, null);
            }
            size++;
            return;
        }
        table[index] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resize() {
        if (size >= (table.length * LOAD_FACTOR)) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = (Node<K, V>[]) new Node[table.length * SCALING_CONSTANT];
            for (Node<K, V> nodes : oldTable) {
                while (nodes != null) {
                    put(nodes.key, nodes.value);
                    nodes = nodes.next;
                }
            }
        }
    }

    public int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
