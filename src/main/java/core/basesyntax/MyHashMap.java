package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MULTIPLIER_FOR_SIZE = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_SIZE];
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (size == 0 || table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        Node<K, V> node = getNode(index, key);
        if (node == null) {
            getLastNode(index).next = newNode;
            size++;
            return;
        }
        node.value = value;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = getNode(index, key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private Node<K, V> getNode(int index, K key) {
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private Node<K, V> getLastNode(int index) {
        Node<K, V> node = table[index];
        while (node.next != null) {
            node = node.next;
        }
        return node;
    }

    private void resize() {
        int oldCap = table.length;
        int newCap = oldCap * MULTIPLIER_FOR_SIZE;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCap];
        size = 0;
        for (Node<K, V> node : oldTable) {
            Node<K, V> oldNode = node;
            while (oldNode != null) {
                put(oldNode.key, oldNode.value);
                oldNode = oldNode.next;
            }
        }
        threshold = (int) (newCap * LOAD_FACTOR);
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }
}
