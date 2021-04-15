package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
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
            currentNode = currentNode.next;
        }
        currentNode.next = new Node<>(key, value);
        size++;

    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        return findNodeValue(currentNode, key);
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private V findNodeValue(Node<K, V> currentNode, K key) {
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;

    }

    private void resizeIfNeeded() {
        if (size >= threshold) {
            int newCapacity;
            newCapacity = table.length << 1;
            threshold = threshold << 1;
            Node<K, V>[] oldTable = table;
            table = new Node[newCapacity];
            size = 0;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
