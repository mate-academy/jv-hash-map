package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= LOAD_FACTOR * table.length) {
            resize();
        }
        int index = calculateIndex(key);
        Node<K, V> currentNode = table[index];
        Node<K, V> newNode = new Node<>(key, value, null);
        if (currentNode == null) {
            table[index] = newNode;
            size++;
            return;
        }
        setNewNode(currentNode, newNode);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[calculateIndex(key)];
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
        Node<K, V>[] oldTable = table;
        size = 0;
        table = new Node[oldTable.length << 1];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int calculateIndex(K key) {
        int hashCode = key == null ? 0 : key.hashCode();
        return Math.abs(hashCode) % table.length;
    }

    private void setNewNode(Node<K, V> currentNode, Node<K, V> newNode) {
        Node<K, V> previousNode = null;
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, newNode.key)) {
                currentNode.value = newNode.value;
                return;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        if (previousNode != null) {
            previousNode.next = newNode;
            size++;
        }
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
