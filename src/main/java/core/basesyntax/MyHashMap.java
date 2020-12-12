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
        table = new Node[capacity];
        size = 0;
    }

    private void resize() {
        capacity = capacity << 1;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] localTable = table;
        table = new Node[capacity];
        for (int i = 0; i < localTable.length; i++) {
            while (localTable[i] != null) {
                put(localTable[i].key, localTable[i].value);
                localTable[i] = localTable[i].next;
                size--;
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

    private boolean checkKeysDuplicate(K key, V value) {
        Node localNode = table[(key == null ? 0 : Math.abs(key.hashCode())) % capacity];
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
        if (checkKeysDuplicate(key, value)) {
            return;
        }
        if (++size > threshold) {
            resize();
        }
        setNode(table, new Node<>(key == null ? 0 : Math.abs(key.hashCode()), key, value, null));
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
