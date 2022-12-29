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
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        checkThreshold();
        Node<K, V> oldNode = table[index];
        while (oldNode.next != null
                && !Objects.equals(key, oldNode.key)) {
            oldNode = oldNode.next;
        }
        if (Objects.equals(key, oldNode.key)) {
            oldNode.value = value;
            return;
        }
        oldNode.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
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

    private void checkThreshold() {
        if (size > threshold) {
            resize();
        }
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        int newLength = table.length * 2;
        threshold = (int) (newLength * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[newLength];
        size = 0;
        transfer(oldTable);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
