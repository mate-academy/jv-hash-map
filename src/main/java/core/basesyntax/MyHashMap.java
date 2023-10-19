package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_GROW = 2;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int capacity;

    public MyHashMap() {
        table = ( Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        capacity = table.length;
    }

    @Override
    public void put(K key, V value) {
        int bucket = findTheBucket(key);
        Node<K, V> currentNode = table[bucket];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        Node<K, V> newNode = new Node<>(key, value,table[bucket]);
        table[bucket] = newNode;
        size++;
        if (size >= capacity * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[findTheBucket(key)];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
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

    private void resize() {
        capacity *= DEFAULT_GROW;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
        transfer(oldTable);
    }

    private int findTheBucket(K key) {
        int bucket = key == null ? 0 : key.hashCode() % capacity;
        return Math.abs(bucket);
    }

    private void transfer(Node<K, V>[] newTable) {
        for (Node<K, V> node : newTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }
}
