package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K,V>[] table;
    private int size;
    private float threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            inreaseTable();
        }
        int hash = getBucketNum(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[hash] == null) {
            table[hash] = newNode;
            size++;
            return;
        }
        Node<K, V> currentNode = table[hash];
        while (currentNode.next != null && !Objects.equals(key,currentNode.key)) {
            currentNode = currentNode.next;
        }
        if (Objects.equals(key, currentNode.key)) {
            currentNode.value = value;
            return;
        }
        currentNode.next = newNode;
        size++;
    }

    private void inreaseTable() {
        int newTableLength = table.length * 2;
        threshold = newTableLength * DEFAULT_LOAD_FACTOR;
        Node<K, V>[] oldTable = table;
        table = new Node[newTableLength];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getBucketNum(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getBucketNum(key)];
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

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
