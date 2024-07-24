package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MULTIPLIER_FOR_INCREASE_CAPACITY = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private int capacity;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        capacity = table.length;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        transferIfNeed();
        int hashOfKey = calculateHash(key);
        int bucketIndex = getBucketIndex(hashOfKey);
        Node<K, V> existingNode = getNode(key);
        if (existingNode != null) {
            existingNode.value = value;
        } else {
            Node<K, V> newNode = new Node<>(hashOfKey, key, value, null);
            insertNode(newNode, bucketIndex);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    private Node<K, V> getNode(K key) {
        int hashOfKey = calculateHash(key);
        int bucketIndex = getBucketIndex(hashOfKey);
        if (table[bucketIndex] == null) {
            return null;
        }
        Node<K, V> current = table[bucketIndex];
        while (current != null) {
            if (current.hash == hashOfKey
                    && (current.key == key || Objects.equals(current.key, key))) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    private void insertNode(Node<K, V> newNode, int bucketIndex) {
        if (table[bucketIndex] == null) {
            table[bucketIndex] = newNode;
        } else {
            getLastNode(bucketIndex).next = newNode;
        }
    }

    private Node<K, V> getLastNode(int bucketIndex) {
        Node<K, V> current = table[bucketIndex];
        while (current.next != null) {
            current = current.next;
        }
        return current;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public V remove(K key) {
        Node<K, V> neededNode = getNode(key);
        if (neededNode == null) {
            return null;
        }
        int bucketIndex = getBucketIndex(neededNode.hash);
        Node<K, V> previous = null;
        Node<K, V> current = table[bucketIndex];
        while (current != null) {
            if (Objects.equals(neededNode, current)) {
                V oldValue = current.value;
                if (previous != null) {
                    previous.next = current.next;
                } else {
                    table[bucketIndex] = current.next;
                }
                size--;
                return oldValue;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        for (Node<K, V> node : table) {
            Node<K, V> current = node;
            while (current != null) {
                if (Objects.equals(current.value, value)) {
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return getSize() == 0;
    }

    private int calculateHash(K key) {
        return key != null ? Objects.hash(key) : 0;
    }

    private int getBucketIndex(int hash) {
        return Math.abs(hash % capacity);
    }

    private void transferIfNeed() {
        if (size == threshold) {
            capacity *= MULTIPLIER_FOR_INCREASE_CAPACITY;
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            Node<K, V>[] oldTable = table;
            table = new Node[capacity];
            size = 0;
            replaceNodes(oldTable);
        }
    }

    private void replaceNodes(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            Node<K, V> current = node;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Node<?, ?> node)) {
                return false;
            }
            return hash == node.hash && Objects.equals(key, node.key)
                    && Objects.equals(value, node.value) && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hash, key, value, next);
        }
    }
}
