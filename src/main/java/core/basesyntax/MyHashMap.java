package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int MULTIPLIER = 2;

    private int size;
    private int threshold;

    private Node<K, V>[] table;

    public static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        checkKey(newNode);
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> getNode = table[index];
        while (getNode != null) {
            if (Objects.equals(getNode.key, key)) {
                return getNode.value;
            }
            getNode = getNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resize() {
        size = 0;
        int newThreshold = threshold * MULTIPLIER;
        int newCapacity = table.length * MULTIPLIER;
        threshold = newThreshold;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void checkKey(Node<K, V> current) {
        int index = hash(current.key);
        Node<K, V> checkNode = table[index];
        while (checkNode.next != null) {
            if (Objects.equals(current.key, checkNode.key)) {
                checkNode.value = current.value;
                return;
            }
            checkNode = checkNode.next;
        }
        if (Objects.equals(current.key, checkNode.key)) {
            checkNode.value = current.value;
            return;
        }
        checkNode.next = current;
        size++;
    }
}
