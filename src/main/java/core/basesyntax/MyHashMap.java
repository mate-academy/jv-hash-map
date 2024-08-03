package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = Math.abs(hash(key));
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        if (table[index] != null) {
            for (Node<K, V> currentNode = table[index]; currentNode != null;
                    currentNode = currentNode.next) {

                if ((currentNode.hash == newNode.hash && currentNode.key == newNode.key)
                        || (currentNode.key == newNode.key)
                        || (currentNode.key != null && currentNode.key.equals(newNode.key))) {

                    currentNode.value = newNode.value;
                    break;
                } else if (currentNode.next == null) {
                    currentNode.next = newNode;
                    size++;
                }
            }
        } else {
            table[index] = newNode;
            size++;
        }
        if (size > threshold) {
            table = resize();
        }

    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(hash(key));
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            return null;
        }
        while (!Objects.equals(currentNode.key, key)) {
            currentNode = currentNode.next;
        }
        return currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode() % DEFAULT_INITIAL_CAPACITY;
    }

    private Node<K, V>[] resize() {
        int newCapacity = table.length * GROW_FACTOR;
        threshold = (int) (LOAD_FACTOR * newCapacity);
        Node<K, V>[] oldTable = table;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                for (Node<K, V> currentNode = oldTable[i]; currentNode != null;
                        currentNode = currentNode.next) {
                    int newTableNodePosition = Math.abs(currentNode.hash) % newCapacity;
                    newTable[newTableNodePosition] = currentNode;
                    break;
                }
            }
        }
        return newTable;
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
            if (o == null || getClass() != o.getClass()) {
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
            return Objects.hash(hash, key, value, next);
        }
    }
}
