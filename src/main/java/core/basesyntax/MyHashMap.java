package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> valueNode = getNode(key);
        if (size >= threshold) {
            resize();
        }
        if (valueNode != null) {
            valueNode.value = value;
            return;
        }
        addNode(calculateIndex(key), key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> desiredNode = getNode(key);
        while (desiredNode != null) {
            if (Objects.equals(key, desiredNode.key)) {
                return desiredNode.value;
            }
            desiredNode = desiredNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int calculateIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private Node<K, V> getNode(K key) {
        int hash = calculateIndex(key);
        Node<K, V> node = table[hash % table.length];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void addNode(int hash, K key, V value) {
        int index = hash % table.length;
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> node = table[index];
            while (node.next != null) {
                node = node.next;
            }
            node.next = newNode;
        }
    }

    private void resize() {
        int newCapacity = table.length << 1;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                addNode(calculateIndex(node.key), node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
