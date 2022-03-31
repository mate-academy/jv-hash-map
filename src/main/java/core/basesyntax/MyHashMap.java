package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_THRESHOLD = 0.75;
    private Node<K, V>[] table;
    private int capacity;
    private double loadThreshold;
    private int size;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        loadThreshold = DEFAULT_LOAD_THRESHOLD;
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        int keyHash = hash(key);
        int indexOfBucket = keyHash % capacity;
        if (table[indexOfBucket] == null) {
            table[indexOfBucket] = new Node<>(keyHash, key, value, null);
            size++;
        } else {
            boolean foundSameKey = false;
            Node<K, V> currentNode = table[indexOfBucket];
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                foundSameKey = true;
            }
            while (currentNode.next != null && !foundSameKey) {
                currentNode = currentNode.next;
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    foundSameKey = true;
                }
            }
            if (!foundSameKey) {
                currentNode.next = new Node<>(keyHash, key, value, null);
                size++;
            }
        }
        if (size > capacity * loadThreshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> currentNode : table) {
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
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

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[capacity * 2];
        size = 0;
        capacity *= 2;
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
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
