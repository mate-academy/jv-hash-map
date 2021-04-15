package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int MULTIPLIER = 2;
    private static final float LOAD_FACTOR = 0.75f;

    private int threshold;
    private int size;
    private Node<K, V>[] table;

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
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
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(Objects.hash(key), key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            putValue(newNode);
        }
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
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

    private void resize() {
        size = 0;
        threshold *= MULTIPLIER;
        int newCapacity = table.length * MULTIPLIER;
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void putValue(Node<K, V> node) {
        int index = getIndex(node.key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, node.key)) {
                current.value = node.value;
                return;
            }
            if (current.next == null) {
                current.next = node;
                size++;
            }
            current = current.next;
        }
    }
}
