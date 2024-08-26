package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int INITIAL_DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MULTIPLICATION_FACTOR = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int bucket = getHash(key);
        Node<K, V> currentNode = table[bucket];
        Node<K, V> newNode = new Node<>(key, value, null);

        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        table[bucket] = newNode;
        size++;

    }

    @Override
    public V getValue(K key) {

        if (size == 0) {
            return null;
        }
        int bucket = getHash(key);
        Node<K, V> node = table[bucket];
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

    private int getHash(K key) {
        return (key == null ? 0 : Math.abs(key.hashCode() % table.length));
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * MULTIPLICATION_FACTOR];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
