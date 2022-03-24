package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private static class Node<K, V> {
        private final K key;
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
        if (checkSize()) {
            resize();
        }
        int indexOfBucket = getIndex(key);
        Node<K, V> node = table[indexOfBucket];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
        table[indexOfBucket] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getIndex(key);
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

    public boolean checkSize() {
        return size >= threshold;
    }

    private int getIndex(K key) {
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

