package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private transient int size;
    private int capacity;
    private int threshold;
    private transient Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        int index = newNode.hash % capacity;
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        Node<K, V> findNode = getNodeByKey(key);
        if (findNode != null) {
            findNode.value = value;
        } else {
            findNode = table[index];
            while (findNode.next != null) {
                findNode = findNode.next;
            }
            findNode.next = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = getNodeByKey(key);
        if (current != null) {
            return current.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        capacity = capacity * 2;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        size = 0;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private Node<K, V> getNodeByKey(K key) {
        int index = hash(key) % capacity;
        Node<K, V> current = table[index];
        if (current != null) {
            if (hash(key) == hash(current.key) && Objects.equals(current.key, key)) {
                return current;
            } else {
                while (current.next != null) {
                    if (hash(key) == hash(current.next.key)
                            && Objects.equals(current.next.key, key)) {
                        return current.next;
                    }
                    current = current.next;
                }
            }
        }
        return null;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
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
    }
}
