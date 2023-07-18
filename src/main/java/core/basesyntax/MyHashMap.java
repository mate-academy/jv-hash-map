package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int RESIZE_INDEX = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int threshHold;
    private Node<K, V>[] hashTable;

    public MyHashMap() {
        hashTable = new Node[DEFAULT_CAPACITY];
        threshHold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        if (hashTable[index] == null) {
            Node<K, V> newNode = new Node<>(key, value);
            hashTable[index] = newNode;
            size++;
        }
        Node<K, V> node = hashTable[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = hashTable[index];
        hashTable[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> getNode = hashTable[getIndex(key)];
        while (getNode != null) {
            if (Objects.equals(getNode.key, key)) {
                return getNode.value;
            }
            getNode = getNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return Objects.hash(key) % hashTable.length;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(hash(key));
    }

    private void resize() {
        if (size == threshHold) {
            int newCapacity = hashTable.length * RESIZE_INDEX;
            threshHold = (int) (hashTable.length * LOAD_FACTOR);
            size = 0;
            Node<K, V>[] oldTable = hashTable;
            hashTable = new Node[newCapacity];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }
}
