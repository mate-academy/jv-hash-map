package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULTL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size;
    private final int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULTL_CAPACITY];
        threshold = (int) (DEFAULTL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        Node<K, V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        table[getIndex(key)] = new Node<>(key, value);
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
            size = 0;
            Node<K, V>[] newNodes = table;
            table = new Node[newCapacity];
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
