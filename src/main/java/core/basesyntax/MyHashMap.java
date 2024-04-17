package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private static final int SIZE_INCREASE_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        growIfHashMapFull();
        int index = getValidIndex(key, table.length);
        Node<K, V> currentNode = table[index];
        Node<K, V> newNode = new Node<>(key, value, null);
        if (currentNode == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> nodeWithUniqueKey = updateIfKeysEqual(currentNode, key, value);
            if (nodeWithUniqueKey != null) {
                nodeWithUniqueKey.next = newNode;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getValidIndex(key, table.length);
        Node<K, V> node = getNode(index);
        if (node != null) {
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void growIfHashMapFull() {
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    private int getValidIndex(K key, int capacity) {
        return (capacity - 1) & hash(key,capacity);
    }

    private int hash(K key, int capacity) {
        return (key == null) ? 0 : key.hashCode() % capacity;
    }

    private Node<K, V> updateIfKeysEqual(Node<K, V> node, K key, V value) {
        while (node.next != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return null;
            }
            node = node.next;
        }
        if (Objects.equals(key, node.key)) {
            node.value = value;
            return null;
        }
        return node;
    }

    private Node<K, V> getNode(int index) {
        return table[index] != null ? table[index] : null;
    }

    private void resize() {
        int newCapacity = table.length * SIZE_INCREASE_FACTOR;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> node : table) {
            if (node != null) {
                moveToNewTable(node, newTable);
            }
        }
        table = newTable;
    }

    private void moveToNewTable(Node<K, V> oldNode, Node<K, V>[] newTable) {
        while (oldNode != null) {
            int index = getValidIndex(oldNode.key, newTable.length);
            Node<K, V> newNodePosition = newTable[index];
            Node<K, V> newNode = new Node<>(oldNode.key, oldNode.value, null);
            if (newNodePosition == null) {
                newTable[index] = newNode;
            } else {
                Node<K, V> tailNode = getTailNode(newNodePosition);
                tailNode.next = newNode;
            }
            oldNode = oldNode.next;
        }
    }

    private Node<K, V> getTailNode(Node<K, V> node) {
        while (node.next != null) {
            node = node.next;
        }
        return node;
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
