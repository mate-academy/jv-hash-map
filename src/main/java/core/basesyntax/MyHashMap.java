package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        if (putValue(key, value, table, getIndex(key, table.length))) {
            size++;
        }
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

    private boolean putValue(K key, V value, Node<K, V>[] nodes, int index) {
        Node<K, V> currentNode = table[index];
        if (nodes[index] == null) {
            nodes[index] = new Node<>(key, value);
            return true;
        }
        while (!Objects.equals(currentNode.key, key)) {
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value);
                return true;
            }
            currentNode = currentNode.next;
        }
        currentNode.value = value;
        return false;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = table.length;
        int newCapacity = oldCapacity * 2;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);

        Node<K, V>[] newNodes = new Node[newCapacity];
        table = newNodes;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                putValue(node.key, node.value, newNodes, getIndex(node.key, newNodes.length));
                node = node.next;
            }
        }
        table = newNodes;
    }

    private int getIndex(K key, int capacity) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
