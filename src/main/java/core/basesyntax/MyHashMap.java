package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int size;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private Node<K, V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @Override
    public void put(K key, V value) {

        int index = hash(key);
        if (size >= threshold) {
            resize();
        }
        Node<K, V> node = table[index];
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
        table[index] = new Node<>(key, value, null);
        size++;
    }

    private void resize() {
        size = 0;
        threshold *= 2;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity * 2];
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> node = table[index];
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
}

