package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final float DEFAULT_RESIZE_FACTOR = 0.75f;
    private static final int DEFAULT_SIZE = 0;
    private Node<K, V>[] table;
    private int capacity;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_RESIZE_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(getBucketIndex(key), key, value);
        int index = getBucketIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    break;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    size++;
                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getBucketIndex(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        if (size >= threshold) {
            size = DEFAULT_SIZE;
            capacity = capacity << 1;
            threshold = (int) (capacity * DEFAULT_RESIZE_FACTOR);
            Node<K, V>[] oldTable = table;
            table = new Node[capacity];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
