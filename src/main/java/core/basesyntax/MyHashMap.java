package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int INCREASE = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * table.length);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = hash(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node(hash(key), key, value, null);
                size++;
            }
            currentNode = currentNode.next;
        }
        table[index] = new Node(hash(key), key, value, null);
        size++;
    }

    public void resize() {
        int newCapacity;
        newCapacity = table.length * INCREASE;
        threshold = threshold * INCREASE;
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;

        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[hash(key)];
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

    private static class Node<K, V> {
        private int hash;
        private final K key;
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

