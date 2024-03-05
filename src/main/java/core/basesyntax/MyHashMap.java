package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int CAPACITY_INCREASE = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table = new Node[DEFAULT_CAPACITY];
    private int size;

    @Override
    public void put(K key, V value) {
        checkThreshold();
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int bucketIndex = getBucketIndex(hash(key));
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[bucketIndex] == null) {
            table[bucketIndex] = newNode;
        } else {
            connectNode(newNode, bucketIndex);
            return;
        }
        Node<K, V> currentNode = table[bucketIndex];
        if (currentNode.key.equals(key)) {
            currentNode.value = value;
        } else {
            currentNode.next = newNode;
        }
        size++;
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
            int bucketIndex = getBucketIndex(hash(key));
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

    private void checkThreshold() {
        if (size > DEFAULT_CAPACITY * LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] newTable = new Node[DEFAULT_CAPACITY * CAPACITY_INCREASE];
        for (Node<K, V> node : table) {
            while (node != null) {
                int bucketIndex = getBucketIndex(hash(node.key));
                Node<K, V> next = node.next;
                node.next = newTable[bucketIndex];
                newTable[bucketIndex] = node;
                node = next;
            }
        }
        table = newTable;
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

    private int getBucketIndex(int hash) {
        return Math.abs(hash) % DEFAULT_CAPACITY;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private void putForNullKey(V value) {
        if (table[0] == null) {
            table[0] = new Node<>(null, value, null);
            size++;
        } else {
            Node<K, V> currentNode = table[0];
            while (currentNode.next != null) {
                if (currentNode.key == null) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            if (currentNode.key == null) {
                currentNode.value = value;
            } else {
                currentNode.next = new Node<>(null, value, null);
                size++;
            }
        }
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
