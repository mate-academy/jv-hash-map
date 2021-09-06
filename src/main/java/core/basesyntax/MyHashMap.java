package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    protected MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        int bucketIndex = getHash(key);
        Node<K, V> node = table[bucketIndex];
        Node<K, V> newNode = new Node<>(key, value, null);
        if (node == null) {
            table[bucketIndex] = newNode;
            size++;
            return;
        }
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
                return;
            }
            node = node.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getHash(key);
        Node<K, V> neededNode = table[bucketIndex];
        while (neededNode != null) {
            if (Objects.equals(neededNode.key, key)) {
                return neededNode.value;
            } else {
                neededNode = neededNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean checkSize() {
        if (size >= threshold) {
            resize();
        }
        return false;
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private Node<K, V>[] resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        threshold = (int) (DEFAULT_LOAD_FACTOR * newCapacity);
        transfer(newTable);
        return newTable;
    }

    private void transfer(Node<K, V>[] newTable) {
        Node<K, V>[] oldTable = table;
        table = newTable;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
                size--;
            }
        }
    }
}
