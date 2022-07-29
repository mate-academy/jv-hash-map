package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int currentCapacity;
    private int size;
    private int threshold;

    @Override
    public void put(K key, V value) {
        if (++size >= threshold || currentCapacity == 0) {
            resize();
        }
        putValue(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int hash = hash(key);
        Node<K, V> currentNode = table[hash];
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

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = table == null ? 0 : table.length;
        int oldThreshold = threshold;
        if (oldCapacity == 0) {
            currentCapacity = DEFAULT_CAPACITY;
            threshold = (int)(DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
            table = new Node[DEFAULT_CAPACITY];
        } else if (size > oldThreshold - 1) {
            currentCapacity = oldCapacity << 1;
            threshold = (int)(currentCapacity * DEFAULT_LOAD_FACTOR);
            table = new Node[currentCapacity];
            transfer(oldTable);
        }
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                putValue(hash(currentNode.key), currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private void putValue(int hash, K key, V value) {
        if (table[hash] == null) {
            table[hash] = new Node<>(hash, key, value);
        } else {
            Node<K, V> currentNode = table[hash];
            while (currentNode.next != null) {
                if (Objects.equals(key, currentNode.key)) {
                    break;
                }
                currentNode = currentNode.next;
            }
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                size--;
            } else {
                currentNode.next = new Node<>(hash, key, value);
            }
        }
    }

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % currentCapacity;
    }
}
