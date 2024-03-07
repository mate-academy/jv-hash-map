package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int CAPACITY_INCREASE = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (checkThreshold()) {
            resize();
        }
        int bucketIndex = getBucketIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[bucketIndex] == null) {
            table[bucketIndex] = newNode;

            size++;
        } else {
            connectNode(newNode, bucketIndex);
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            Node<K, V> currentNode = table[0];
            while (currentNode != null) {
                if (currentNode.key == null) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        } else {
            int bucketIndex = getBucketIndex(key);
            Node<K, V> currentNode = table[bucketIndex];
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

    private boolean checkThreshold() {
        return size == table.length * LOAD_FACTOR;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * CAPACITY_INCREASE];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> next = node.next;
                putInResize(node.key, node.value);
                node = next;
            }
        }
    }

    private void putInResize(K key, V value) {
        if (checkThreshold()) {
            resize();
        }
        int bucketIndex = getBucketIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[bucketIndex] == null) {
            table[bucketIndex] = newNode;
            size++;
        } else {
            connectNode(newNode, bucketIndex);
        }
    }

    private void connectNode(Node<K, V> node, int bucket) {
        Node<K, V> currentNode = table[bucket];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, node.key)) {
                currentNode.value = node.value;
                return;
            }
            if (currentNode.next == null) {
                break;
            }
            currentNode = currentNode.next;
        }
        node.next = currentNode.next;
        currentNode.next = node;
        size++;
    }

    private int getBucketIndex(K key) {
        return Math.abs(hash(key)) % DEFAULT_CAPACITY;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
