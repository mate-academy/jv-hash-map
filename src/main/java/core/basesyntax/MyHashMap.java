package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private Integer size = 0;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            table = resize();
        }
        int bucket = getIndex(key, table);
        Node<K, V> current = table[bucket];
        while (current != null) {
            if (key == current.key || key != null && key.equals(current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        table[bucket] = new Node<>(hash(key), key, value, table[bucket]);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = searchNode(key);
        if (node != null) {
            return node.getValue();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                K key = node.getKey();
                int index = getIndex(key, newTable);
                Node<K, V> newNode = new Node<>(node.hash, key, node.getValue(), null);
                if (newTable[index] != null) {
                    Node<K, V> currentNode = newTable[index];
                    while (currentNode.next != null) {
                        currentNode = currentNode.next;
                    }
                    currentNode.next = newNode;
                } else {
                    newTable[index] = newNode;
                }
                node = node.next;
            }
        }
        return newTable;
    }

    public int hash(Object key) {
        return (key == null) ? 0 : (key.hashCode());
    }

    private Node<K, V> searchNode(K key) {
        int index = getIndex(key, table);
        Node<K, V> node = table[index];
        if (node == null) {
            return null;
        } else {
            do {
                if (key == node.key || key != null && key.equals(node.key)) {
                    return node;
                }
                node = node.next;
            } while (node != null);
        }
        return null;
    }

    private int getIndex(K key, Node<K, V>[] table) {
        return Math.abs(hash(key) % table.length);
    }

    public static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final V getValue() {
            return value;
        }

        public final K getKey() {
            return key;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
    }
}
