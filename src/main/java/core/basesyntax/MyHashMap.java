package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_NUMBER = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        ensureCapacity();

        Node<K, V> node = new Node<>(key, value, null);
        int index = getKeyBucket(key);
        if (table[index] == null) {
            table[index] = node;
            size++;
            return;
        }
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = node;
                size++;
            }
            currentNode = currentNode.next;
        }

    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getKeyBucket(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> 16));
    }

    private int getKeyBucket(K key) {
        if (hash(key) == 0) {
            return 0;
        }
        return Math.abs(hash(key) % table.length);
    }

    private void ensureCapacity() {
        if (size == INITIAL_CAPACITY * LOAD_FACTOR) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[(int) (INITIAL_CAPACITY * GROW_NUMBER)];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Node)) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return hash == node.hash
                    && Objects.equals(key, node.key)
                    && Objects.equals(value, node.value)
                    && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value, hash, next);
        }
    }
}
