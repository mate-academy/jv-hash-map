package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        boolean wasNewNodeCreated = putValue(key, value, table, getIndex(key, table.length));
        if (wasNewNodeCreated) {
            size++;
        }
    }

    private boolean putValue(K key, V value, Node<K, V>[] nodes, int index) {
        if (nodes[index] == null) {
            nodes[index] = new Node<>(key, value, null);
            return true;
        }
        Node<K, V> currentNode = nodes[index];
        while (!Objects.equals(currentNode.key, key)) {
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value, null);
                return true;
            }
            currentNode = currentNode.next;
        }
        currentNode.value = value;
        return false;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key, table.length)];
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
        Node<K, V>[] newNodes = (Node<K, V>[]) new Node[table.length * 2];
        threshold = (int) (newNodes.length * LOAD_FACTOR);
        for (Node<K, V> node : table) {
            while (node != null) {
                int index = getIndex(node.key, newNodes.length);
                putValue(node.key, node.value, newNodes, index);
                node = node.next;
            }
        }
        table = newNodes;
    }

    private int getIndex(K key, int capacity) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
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
