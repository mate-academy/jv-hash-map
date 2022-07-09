package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MULTIPLY_COEFFICIENT = 2;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (threshold == size) {
            resize();
        }
        int index = hash(key);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> foundNode = findNode(node, key);
            if (foundNode != null) {
                foundNode.value = value;
            } else {
                Node<K, V> lastNode = getLastNode(node);
                lastNode.next = new Node(key, value, null);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> node = findNode(table[index], key);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resize() {
        threshold = (int) (table.length * MULTIPLY_COEFFICIENT * LOAD_FACTOR);
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * MULTIPLY_COEFFICIENT];
        for (Node<K, V> oldNode : oldTable) {
            Node<K, V> node = oldNode;
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }

    }

    private Node<K, V> findNode(Node<K, V> node, K key) {
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private int hash(K key) {
        int index = key == null ? 0 : key.hashCode() % table.length;
        if (index < 0) {
            return -(index);
        }
        return index;
    }

    private Node<K, V> getLastNode(Node<K, V> node) {
        while (node.next != null) {
            node = node.next;
        }
        return node;
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
}
