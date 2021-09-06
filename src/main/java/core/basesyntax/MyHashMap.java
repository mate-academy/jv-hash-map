package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;
        private final int hash;

        private Node(K key, V value, Node<K, V> next, int hash) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = hash;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = Math.abs(getHash(key) % table.length);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value, null, getHash(key));
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        table[index] = new Node<>(key, value, null, getHash(key));
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(getHash(key) % table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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
        if (key == null) {
            return 0;
        }
        return key.hashCode();
    }

    private void resize() {
        if (size == threshold) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * 2];
            transfer(oldTable);
        }

    }

    private void transfer(Node<K, V>[] node) {
        for (Node<K, V> currentNode : node) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }
}
