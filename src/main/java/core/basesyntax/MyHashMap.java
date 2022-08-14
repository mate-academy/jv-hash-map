package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    @Override
    public void put(K key, V value) {
        if (++size >= threshold || table.length == 0) {
            resize();
        }
        putValue(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        Node<K, V> currentNode = table[hash(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : table.length;
        int oldThreshold = threshold;
        int newCapacity;
        if (oldCapacity == 0) {
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
            table = new Node[DEFAULT_INITIAL_CAPACITY];
        } else if (size > oldThreshold - 1) {
            newCapacity = oldCapacity << 1;
            threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
            table = new Node[newCapacity];
            transfer(oldTable);
        }
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                putValue(hash(currentNode.key), currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void putValue(int hash, K key, V value) {
        if (table[hash] == null) {
            table[hash] = new Node<>(hash, key, value, null);
        } else {
            Node<K, V> currentNode = table[hash];
            while (currentNode.next != null) {
                if (Objects.equals(currentNode.key, key)) {
                    break;
                }
                currentNode = currentNode.next;
            }
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                size--;
            } else {
                currentNode.next = new Node<>(hash, key, value, null);
            }
        }

    }

    private class Node<K, V> {
        private int hash;
        private K key;
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
