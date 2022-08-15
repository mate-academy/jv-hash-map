package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int capacity;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        capacity = DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        putNode(key, value);
    }

    @Override
    public V getValue(K key) {
        int hash = getBucketIndex(key);
        Node<K, V> currentNode = table[hash];
        if (currentNode != null) {
            while (currentNode.next != null) {
                if (Objects.equals(currentNode.key, key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
            return currentNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        capacity = capacity * 2;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        copyElement(oldTable);
    }

    private void copyElement(Node<K, V>[] oldTable) {
        size = 0;
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                putNode(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private void putNode(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        if (table[bucketIndex] == null) {
            table[bucketIndex] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> currentNode = table[bucketIndex];
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            } else {
                while (currentNode.next != null) {
                    if (Objects.equals(currentNode.key, key)) {
                        break;
                    }
                    currentNode = currentNode.next;
                }
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                } else {
                    currentNode.next = new Node<>(key, value, null);
                    size++;
                }
            }
        }
    }

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
