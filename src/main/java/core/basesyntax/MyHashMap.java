package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROWTH_FACTOR = 2;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = findIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(hash(key), key, value, null);
            size++;
        } else if (findNodeByKey(key, index) == null) {
            Node<K, V> newNode = new Node<>(hash(key), key, value, null);
            Node<K, V> lastNode = getLastNode(index);
            lastNode.next = newNode;
            size++;
        } else {
            Node<K, V> currentNode = findNodeByKey(key, index);
            currentNode.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        int index = findIndex(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (isMatchingKey(currentNode, key)) {
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

    private int findIndex(K key) {
        return hash(key) % table.length;
    }

    private boolean isMatchingKey(Node<K, V> node, K key) {
        return Objects.equals(node.key, key);
    }

    private Node<K, V> findNodeByKey(K key, int index) {
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (isMatchingKey(currentNode, key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private void resize() {
        if (size > (table.length * LOAD_FACTOR)) {
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * GROWTH_FACTOR];
            transfer(oldTable);
        }
    }

    private void transfer(Node<K, V>[] oldNodes) {
        for (int i = 0; i < oldNodes.length; i++) {
            Node<K, V> currentNode = oldNodes[i];
            while (currentNode != null) {
                int newIndex = hash(currentNode.key) % table.length;
                if (table[newIndex] == null) {
                    table[newIndex] = new Node<>(hash(currentNode.key),
                            currentNode.key, currentNode.value, null);
                } else {
                    Node<K, V> newNode = new Node<>(hash(currentNode.key),
                            currentNode.key, currentNode.value, null);
                    Node<K, V> lastNode = getLastNode(newIndex);
                    lastNode.next = newNode;
                }
                currentNode = currentNode.next;
            }
        }
    }

    private Node<K, V> getLastNode(int index) {
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.next == null) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode() & 0x7FFFFFFF);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
