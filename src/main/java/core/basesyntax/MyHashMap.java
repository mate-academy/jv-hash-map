package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = newNode;
            size++;
            return;
        } else {
            addOrUpdateNode(currentNode, newNode);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        int newCapacity = table.length << 1;
        Node<K, V>[] newTable = new Node[newCapacity];
        threshold = (int) (LOAD_FACTOR * newCapacity);
        for (Node<K, V> node : table) {
            while (node != null) {
                int index = node.key == null ? 0 : Math.abs(node.key.hashCode()) % newCapacity;
                Node<K, V> next = node.next;
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
    }

    private void addOrUpdateNode(Node<K, V> currentNode, Node<K, V> newNode) {
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
        } else {
            table[getIndex(newNode.key)] = newNode;
        }
        size++;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
