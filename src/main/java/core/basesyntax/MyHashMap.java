package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private float threshold;
    private int size;
    private Node<K, V>[] table;

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> nextNode;

        Node(int hash, K key, V value, Node<K, V> nextNode) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * table.length);
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node(getIndex(key), key, value, null);
        if (table[getIndex(key)] == null) {
            table[getIndex(key)] = newNode;
            size++;
            return;
        }
        checkKey(newNode, key);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> newNode = checkKey(null, key);
        return (newNode == null) ? null : newNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> checkKey(Node<K, V> newNode, K key) {
        Node<K, V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if ((currentNode.key == key) || (key != null && Objects.equals(currentNode.key, key))) {
                if (newNode != null) {
                    currentNode.value = newNode.value;
                }
                return currentNode;
            }
            if (currentNode.nextNode == null) {
                currentNode.nextNode = newNode;
                size++;
            }
            currentNode = currentNode.nextNode;
        }
        return null;
    }

    private void resize() {
        if (size == threshold) {
            size = 0;
            threshold *= 2;
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * 2];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.nextNode;
                }
            }
        }
    }
    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }
}
