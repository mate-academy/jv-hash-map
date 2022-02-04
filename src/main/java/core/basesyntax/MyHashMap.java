package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_SIZE_ARRAY = 16;
    private static final int CAPACITY_INCREASE = 2;
    private static final int ZERO_SIZE = 0;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table = new Node[INITIAL_SIZE_ARRAY];
    private int size;
    private int capacity = INITIAL_SIZE_ARRAY;

    private static class Node<K, V> {
        private final int hash;
        private Node<K, V> next;
        private final K key;
        private V value;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.next = next;
            this.value = value;
            this.key = key;
        }

        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key) && Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    static int hash(Object key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(31 * key.hashCode());
    }

    private Node<K, V> getNode(K key, V value) {
        return new Node<>(hash(key), key, value, null);
    }

    private int getArrayPosition(int hash) {
        return hash % capacity;
    }

    private void addIntoCollision(K key, V value, int position) {
        Node<K, V> nodeIntoCollision = table[position];
        while (nodeIntoCollision != null) {
            if (nodeIntoCollision.key == key
                    || (nodeIntoCollision.key != null && nodeIntoCollision.key.equals(key))) {
                nodeIntoCollision.setValue(value);
                return;
            }
            if (nodeIntoCollision.next == null) {
                nodeIntoCollision.next = getNode(key, value);
                size++;
            }
            nodeIntoCollision = nodeIntoCollision.next;
        }
    }

    private void remap() {
        capacity *= CAPACITY_INCREASE;
        size = ZERO_SIZE;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= capacity * LOAD_FACTOR) {
            remap();
        }
        int hashCode = hash(key);
        int index = getArrayPosition(hashCode);
        if (table[index] != null) {
            addIntoCollision(key, value, index);
        } else {
            table[index] = getNode(key, value);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getArrayPosition(hash(key))];
        while (node != null) {
            if (node.key == key || (node.key != null && node.key.equals(key))) {
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

}
