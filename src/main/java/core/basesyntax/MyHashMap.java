package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private Node<K, V> node;
    private int index;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        index = hash(key);
        Node<K, V> newNode = createNode(key, value);
        node = table[index];

        if (node == null) {
            table[index] = newNode;
            size++;
            return;
        }

        while (node.next != null) {
            if (Objects.equals(node.key, newNode.key)) {
                node.value = newNode.value;
                return;
            }
            node = node.next;
        }

        if (Objects.equals(node.key, newNode.key)) {
            node.value = newNode.value;
            return;
        }

        node.next = newNode;
        size++;

        if (size == table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        index = hash(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    private Node<K, V> createNode(K key, V value) {
        return new Node<>(hash(key), key, value, null);
    }

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        int newCapacity = table.length << 1;
        Node<K, V> [] oldTable = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> oldNode: oldTable) {
            while (oldNode != null) {
                put(oldNode.key, oldNode.value);
                oldNode = oldNode.next;
            }
        }
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
