package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        boolean addedNewNode = putNodeInTable(new Node<>(key, value));
        if (addedNewNode) {
            size++;
        }
    }

    @Override
    public V getValue(K key) {
            Node<K, V> currentNode = table[getIndex(key)];
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

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private boolean putNodeInTable(Node<K, V> node) {
        int index = getIndex(node.key);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = node;
        } else {
            Node<K, V> prevNode = null;
            while (currentNode != null) {
                if (Objects.equals(node.key, currentNode.key)) {
                    currentNode.value = node.value;
                    return false;
                }
                prevNode = currentNode;
                currentNode = currentNode.next;
            }
            prevNode.next = node;
        }
        return true;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        threshold = threshold * 2;
        table = new Node[oldTable.length * 2];
        transferNodes(oldTable);
    }

    private int getIndex(K key) {
        return hash(key) % table.length;
    }

    private void transferNodes(Node<K, V>[] oldTable) {
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                putNodeInTable(new Node<>(currentNode.key, currentNode.value));
                currentNode = currentNode.next;
            }
        }
    }
}
