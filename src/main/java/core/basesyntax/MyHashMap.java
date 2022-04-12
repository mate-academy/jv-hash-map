package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == DEFAULT_LOAD_FACTOR * table.length) {
            resize();
        }
        Node<K, V> node = new Node<>(key, value, null);
        int index = getHash(key) % table.length;
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = node;
            size++;
        } else {
            while (currentNode.next != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
            } else {
                currentNode.next = node;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getHash(key) % table.length;
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
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
        Node<K, V>[] current = table;
        size = 0;
        table = new Node[current.length * 2];
        for (Node<K, V> node : current) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
