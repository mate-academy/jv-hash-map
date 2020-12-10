package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int TABLE_MULTIPLIER = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int size = 0;
    private int capacity;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (LOAD_FACTOR * capacity);
        table = new Node[capacity];
    }

    private void resize() {
        Node<K, V>[] localTable = new Node[capacity * TABLE_MULTIPLIER];
        capacity *= TABLE_MULTIPLIER;
        threshold = (int) (capacity * LOAD_FACTOR);
        for (int i = 0; i < capacity / 2; i++) {
            if (table[i] != null) {
                Node<K, V> localNode = table[i];
                setNodes(localTable, localNode);
            }
        }
        table = localTable;
    }

    private void setNodes(Node<K, V>[] localTable, Node node) {
        Node<K, V> localNode = node;
        while (localNode != null) {
            int hash = (localNode.key == null) ? 0 : Math.abs(localNode.key.hashCode());
            if (localTable[hash % capacity] == null) {
                int i = hash % capacity;
                localTable[i] = localNode;
                localNode = localNode.next;
                localTable[i].next = null;
            } else {
                Node<K, V> localNode1 = localTable[hash % capacity];
                while (localNode1.next != null) {
                    localNode1 = localNode1.next;
                }
                localNode1.next = localNode;
                localNode = localNode.next;
                localNode1.next.next = null;
            }
        }
    }

    private void setNode(Node<K, V>[] localTable, Node node) {
        Node<K, V> localNode = localTable[node.hash % capacity];
        if (localNode == null) {
            node.next = null;
            localTable[node.hash % capacity] = node;
            return;
        }
        while (localNode.next != null) {
            localNode = localNode.next;
        }
        node.next = null;
        localNode.next = node;
    }

    private boolean checkKeys(K key, V value) {
        int hash = (key == null) ? 0 : Math.abs(key.hashCode());
        Node localNode = table[hash % capacity];
        while (localNode != null) {
            if (Objects.equals(localNode.key, key)) {
                localNode.value = value;
                return true;
            }
            localNode = localNode.next;
        }
        return false;
    }

    @Override
    public void put(K key, V value) {
        int hash = 0;
        if (key != null) {
            hash = Math.abs(key.hashCode());
        }
        if (size == 0) {
            size++;
            table[hash % capacity] = new Node<>(hash, key, value, null);
            return;
        }
        if (checkKeys(key, value)) {
            return;
        }
        if (++size > threshold) {
            resize();
        }
        setNode(table, new Node<>(hash, key, value, null));
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int hash = key == null ? 0 : Math.abs(key.hashCode());
        Node<K, V> localNode = table[hash % capacity];
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

    class Node<K, V> {
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
    }
}
