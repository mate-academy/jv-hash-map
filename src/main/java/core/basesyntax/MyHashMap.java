package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private Node<K, V>[] table;
    private float threshold;
    private int size;

    protected MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value, null);
        resize();
        int index = getHashCode(hashNode(node.key));
        if (table[index] != null) {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = node;
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
        table[index] = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getHashCode(hashNode(key))];
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

    private int getHashCode(int hash) {
        return hash % table.length;
    }

    private void resize() {
        threshold = table.length * DEFAULT_LOAD_FACTOR;
        if (size == threshold) {
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * RESIZE_FACTOR];
            size = 0;
            for (Node<K, V> node : oldTable) {
                if (node != null) {
                    Node<K, V> currentNode = node;
                    while (currentNode != null) {
                        put(currentNode.key, currentNode.value);
                        currentNode = currentNode.next;
                    }
                }
            }
        }
    }

    private int hashNode(K key) {
        return key == null ? 0 : key.hashCode() < 0 ? -key.hashCode() : key.hashCode();
    }
}
