package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int SIZE_MULTIPLIER = 2;
    private int threshold;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        createNewTable();
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> indexNode = table[index];
        Node<K, V> newNode = new Node<>(getHash(key), key, value, null);
        if (indexNode == null) {
            table[index] = newNode;
        } else {
            Node<K, V> previousNode = null;
            while (indexNode != null) {
                if (Objects.equals(indexNode.key, key)) {
                    indexNode.value = value;
                    return;
                }
                previousNode = indexNode;
                indexNode = indexNode.next;
            }
            previousNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        if (node == null) {
            return null;
        }
        while (!Objects.equals(node.key, key)) {
            node = node.next;
        }
        return node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNode(Object key) {
        return table[getIndex(key)];
    }

    private int getIndex(Object key) {
        int index = Math.abs(getHash(key) % table.length);
        return index;
    }

    private int getHash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldInnerArray = table;
        table = new Node[table.length * SIZE_MULTIPLIER];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (Node<K, V> node : oldInnerArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private Node<K, V>[] createNewTable() {
        Node<K, V>[] initialNodeArray = new Node[DEFAULT_CAPACITY];
        table = initialNodeArray;
        threshold = (int) (table.length * LOAD_FACTOR);
        return initialNodeArray;
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
