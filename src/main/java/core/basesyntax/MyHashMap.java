package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULTL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULTL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= DEFAULTL_CAPACITY * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        Node<K, V> currentNode = table[getIndex(key)];
        if (currentNode == null) {
            table[getIndex(key)] = new Node<>(key, value);
            size++;
            return;
        }
        while (currentNode.next != null || Objects.equals(currentNode.key, key)) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value);
                size++;
            }
            currentNode = currentNode.next;
        }
        currentNode.next = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
        if (currentNode == null) {
            return null;
        }
        while (!Objects.equals(currentNode.key, key)) {
            currentNode = currentNode.next;
        }
        return currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            int newCapacity = table.length * 2;
            Node<K, V>[] oldNodes = new Node[newCapacity];
            Node<K, V>[] newNodes = table;
            table = oldNodes;
            size = 0;
            for (Node<K, V> node: newNodes) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
