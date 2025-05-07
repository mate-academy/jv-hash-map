package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putNullValue(value);
            return;
        }

        int index = getBucketIndex(key);

        if (table[index] == null) {
            table[index] = new Node<>(key, value);
        } else {
            Node<K, V> currentNode = table[index];
            Node<K, V> prevNode = null;
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                prevNode = currentNode;
                currentNode = currentNode.next;
            }
            prevNode.next = new Node<>(key, value);
        }
        size++;

        if (size >= table.length * LOAD_FACTOR) {
            resize();
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

    private void putNullValue(V value) {
        if (table[0] == null) {
            table[0] = new Node<>(null, value);
            size++;
        } else {
            Node<K, V> currentNode = table[0];
            while (currentNode != null) {
                if (currentNode.key == null) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(null, value);
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
    }

    private int getBucketIndex(K key) {
        return Math.abs(Objects.hashCode(key)) % table.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        size = 0;
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
