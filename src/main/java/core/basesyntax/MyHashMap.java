package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int INITIAL_CAPACITY = 16;
    public static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        if (table[hash(key)] == null) {
            Node currentNode = new Node(key, value, null);
            table[hash(key)] = currentNode;
        } else {
            Node<K, V> lastNode = table[hash(key)];
            if (isKeyEquals(lastNode, key, value)) {
                return;
            }
            while (lastNode.next != null) {
                if (isKeyEquals(lastNode, key, value)) {
                    return;
                }
                lastNode = lastNode.next;
            }
            if (isKeyEquals(lastNode, key, value)) {
                return;
            }
            Node<K, V> currentNode = new Node<>(key, value, null);
            lastNode.next = currentNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        Node<K, V> currentNode = table[hash(key)];
        if (currentNode != null) {
            while (!Objects.equals(currentNode.key, key)) {
                if (currentNode.next == null) {
                    return null;
                }
                currentNode = currentNode.next;
            }
            return (V) currentNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        int result = key == null ? 0 : key.hashCode() % table.length;
        return result < 0 ? result * -1 : result;
    }

    private void resize() {
        if (size == table.length * LOAD_FACTOR) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * 2];
            for (Node<K, V> node : oldTable) {
                if (node != null) {
                    Node<K, V> currentNode = node;
                    while (currentNode != null) {
                        put(currentNode.key, currentNode.value);
                        currentNode = currentNode.next;
                    }
                }
            }
        }
    }

    private boolean isKeyEquals(Node<K, V> node, K key, V value) {
        if (Objects.equals(node.key, key)) {
            node.value = value;
            return true;
        }
        return false;
    }
}
