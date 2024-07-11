package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int MULTIPLIER = 2;
    private int size;
    private Node<K, V>[] table;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_SIZE];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        Node<K, V> newNode = new Node<>(key, value, null);

        if (currentNode == null) {
            table[index] = newNode;
            size++;
            return;
        }

        Node<K, V> previousNode = null;
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }

        previousNode.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int index = getIndex(key);
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

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * MULTIPLIER];
        size = 0;
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
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
