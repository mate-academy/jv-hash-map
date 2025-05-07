package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resizeTables();
        }
        Node<K, V> newNode = new Node<>(getHash(key),key, value,null);
        int bucketIndex = getBucketIndex(key);
        if (table[bucketIndex] == null) {
            table[bucketIndex] = newNode;
            size++;
        } else {
            linkNode(table[bucketIndex], newNode);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[getBucketIndex(key)];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        int hash = key == null ? 0 : key.hashCode();
        if (hash < 0) {
            hash *= -1;
        }
        return hash;
    }

    private int getBucketIndex(K key) {
        int position = getHash(key) % table.length;
        return position;
    }

    private void resizeTables() {
        final Node<K, V>[] temp = table;
        table = (Node<K, V>[]) new Node[table.length << 1];
        size = 0;
        updateThreshold();
        for (Node<K, V> node : temp) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void updateThreshold() {
        threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);
    }

    private void linkNode(Node<K,V> kvNode, Node<K,V> newNode) {
        if (kvNode.key == newNode.key || kvNode.key.equals(newNode.key)) {
            kvNode.value = newNode.value;
            return;
        }
        while (kvNode.next != null) {
            if (kvNode.key == newNode.key) {
                kvNode.value = newNode.value;
                return;
            }
            kvNode = kvNode.next;
        }
        if (kvNode.key == newNode.key || kvNode.key != null && kvNode.key.equals(newNode.key)) {
            kvNode.value = newNode.value;
            return;
        }
        kvNode.next = new Node<>(getHash(newNode.key), newNode.key, newNode.value, null);
        size++;
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
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
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<K, V> node = (Node<K, V>) o;
            return hash == node.hash && Objects.equals(key, node.key)
                    && Objects.equals(value, node.value)
                    && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hash, key, value, next);
        }
    }
}
