package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int capacity;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (LOAD_FACTOR * capacity);
        table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        setNode(new Node<>(key, value, null));
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        Node<K, V> localNode = table[getHash(key) % capacity];
        while (localNode != null) {
            if (Objects.equals(localNode.key, key)) {
                return localNode.value;
            }
            localNode = localNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void resize() {
        capacity = capacity << 1;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] localTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
        for (int i = 0; i < localTable.length; i++) {
            while (localTable[i] != null) {
                put(localTable[i].key, localTable[i].value);
                localTable[i] = localTable[i].next;
            }
        }
    }

    private void setNode(Node<K, V> node) {
        int indexOfBucket = getHash(node.key) % capacity;
        Node<K, V> localNode = table[indexOfBucket];
        if (localNode == null) {
            table[indexOfBucket] = node;
            size++;
            return;
        }
        while (localNode.next != null) {
            if (Objects.equals(localNode.key, node.key)) {
                localNode.value = node.value;
                return;
            }
            localNode = localNode.next;
        }
        if (Objects.equals(localNode.key, node.key)) {
            localNode.value = node.value;
            return;
        }
        localNode.next = node;
        size++;
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
