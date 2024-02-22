package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_FACTOR = 2;
    private static final int EMPTY_SIZE = 0;
    private static final int NULL_KEY_HASHCODE = 0;
    private int size;
    private Node<K, V>[] table;
    private int threshold;

    private static class Node<K, V> {
        private final K key;
        private int hash;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        if (table == null) {
            table = getTable(INITIAL_CAPACITY);
        }
        Node<K, V> nodeToPut;
        if (size != EMPTY_SIZE) {
            nodeToPut = findSameKey(key);
            if (nodeToPut != null) {
                nodeToPut.value = value;
                return;
            }
        }
        nodeToPut = new Node<>(key, value);
        nodeToPut.hash = getHashCode(key);
        int index = getIndex(nodeToPut.hash);
        if (size + 1 == threshold) {
            resize();
        }
        putNode(nodeToPut, index);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        Node<K,V> node = findSameKey(key);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    private Node<K,V>[] getTable(int capacity) {
        threshold = (int) (capacity * LOAD_FACTOR);
        return new Node[capacity];
    }

    private int getIndex(int hashCode) {
        return hashCode % table.length;
    }

    private int getHashCode(K key) {
        return key == null ? NULL_KEY_HASHCODE : Math.abs(key.hashCode());
    }

    private void resize() {
        Node<K, V>[] tableCopy = table;
        table = getTable(table.length * RESIZE_FACTOR);
        int index;
        for (Node<K, V> bucket : tableCopy) {
            Node<K, V> node = bucket;
            while (node != null) {
                index = getIndex(node.hash);
                Node<K, V> nextNode = node.next;
                putNode(node, index);
                size--;
                node = nextNode;
            }
        }
    }

    private void putNode(Node<K,V> node, int index) {
        if (table[index] == null) {
            putInEmptyBucket(node, index);
        } else {
            putInOccupiedBucket(node, index);
        }
    }

    private void putInOccupiedBucket(Node<K,V> node, int index) {
        Node<K,V> lastNode = table[index];
        while (lastNode.next != null) {
            lastNode = lastNode.next;
        }
        lastNode.next = node;
        node.next = null;
        size++;
    }

    private void putInEmptyBucket(Node<K,V> node, int index) {
        table[index] = node;
        node.next = null;
        size++;
    }

    private Node<K,V> findSameKey(K key) {
        int index = getIndex(getHashCode(key));
        for (Node<K,V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
        }
        return null;
    }
}
