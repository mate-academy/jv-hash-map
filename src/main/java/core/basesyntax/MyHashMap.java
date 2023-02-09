package core.basesyntax;

import java.util.Map;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int GROW_FACTOR = 2;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            grow();
        }
        int bucketIndex = getBucketIndex(key, table.length);
        if (table[bucketIndex] == null) {
            table[bucketIndex] = new Node<>(key, value);
        } else {
            Node<K, V> prevNode = table[bucketIndex];
            while (prevNode != null) {
                if (isKeyEqual(key, prevNode.key)) {
                    prevNode.setValue(value);
                    return;
                }
                if (prevNode.next == null) {
                    prevNode.next = new Node<>(key, value);
                    break;
                }
                prevNode = prevNode.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key, table.length);
        if (table[bucketIndex] != null) {
            Node<K, V> node = table[bucketIndex];
            while (node != null) {
                if (isKeyEqual(key, node.key)) {
                    return node.getValue();
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    private int getBucketIndex(K key, int tableLength) {
        int index = key == null ? 0 : key.hashCode() % tableLength;
        if (index < 0) {
            index = -index;
        }
        return index;
    }

    private void grow() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * GROW_FACTOR];
        size = 0;
        for (Node<K, V> kvNode : oldTable) {
            if (kvNode != null) {
                Node<K, V> node = kvNode;
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private boolean isKeyEqual(K newKey, K oldKey) {
        return newKey == oldKey || newKey != null && newKey.equals(oldKey);
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
}
