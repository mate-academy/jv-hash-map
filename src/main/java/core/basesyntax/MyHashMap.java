package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity = DEFAULT_CAPACITY;
    private Node<K,V>[] table = new Node[capacity];
    private int size;

    @Override
    public void put(K key, V value) {
        checkThreshold();
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int hash = hash(key);
        int bucket = getBucketIndex(hash);
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (table[bucket] == null) {
            table[bucket] = newNode;
        } else {
            connectNode(newNode, bucket);
            return;
        }
        Node<K, V> currentNode = table[bucket];
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
            int hash = hash(key);
            int bucket = getBucketIndex(hash);
            Node<K, V> currentNode = table[bucket];
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

    private void putForNullKey(V value) {
        if (table[0] == null) {
            table[0] = new Node<>(0, null, value, null);
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
                currentNode.next = new Node<>(0, null, value, null);
                size++;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private void resize() {
        capacity *= 2;
        Node<K, V>[] newTable = new Node[capacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                int bucket = getBucketIndex(node.hash);
                Node<K, V> next = node.next;
                node.next = newTable[bucket];
                newTable[bucket] = node;
                node = next;
            }
        }
        table = newTable;
    }

    private void checkThreshold() {
        if (size > capacity * LOAD_FACTOR) {
            resize();
        }
    }

    private int getBucketIndex(int hash) {
        return Math.abs(hash) % capacity;
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

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
