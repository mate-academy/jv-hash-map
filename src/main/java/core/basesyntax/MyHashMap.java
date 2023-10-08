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

    private class Node<K, V> {
        private final int hashCode;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hashCode = key == null ? 0 : key.hashCode();
            this.next = next;
        }

    }


    @Override
    public void put(K key, V value) {
        if (size >= LOAD_FACTOR * table.length) {
            resize();
        }
        Node<K, V> currentNode = table[calculateIndex(key)];
        Node<K, V> newNode = new Node<>(key, value, null);
        if (currentNode == null) {
            table[calculateIndex(key)] = newNode;
            size++;
            return;
        }
        connectNode(currentNode, newNode);
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
        int index = Math.abs(hashCode) % table.length;
        return index;
    }

    private void connectNode(Node<K, V> currentNode, Node<K, V> newNode) {
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, newNode.key)) {
                currentNode.value = newNode.value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

}

