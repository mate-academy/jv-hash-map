package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int threshold;
    private int size;

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        int index = hash(key);

        if (table[index] == null) {
            simpleAdd(index, key, value);
            return;
        } else {
            collisionHandling(key, value, index);
        }
    }

    private void collisionHandling(K key, V value, int index) {
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> lastNode = new Node<K, V>(key, value, table[index]);
        table[index] = lastNode;
        size++;
    }

    private void simpleAdd(int index, K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        table[index] = newNode;
        size++;
    }

    private void resize() {
        Node<K, V>[] oldNTable = table;
        table = new Node[oldNTable.length * 2];
        size = 0;
        for (Node<K, V> currentNode : oldNTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        if (table != null && index < table.length && table[index] != null) {
            Node<K, V> node = table[index];
            while (node != null) {
                if (Objects.equals(node.key, key)) {
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

    private int hash(K key) {
        return key != null ? Math.abs(key.hashCode() % table.length) : 0;
    }
}
