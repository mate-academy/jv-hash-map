package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int CAPACITY_MULTIPLIER = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private final float loadFactor;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * INITIAL_CAPACITY);
        loadFactor = DEFAULT_LOAD_FACTOR;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = getNode(key);
        Node<K, V> head;

        int index = indexFor(hash(key), table.length);

        if (node == null) {
            head = table[index];
            table[index] = new Node<>(hash(key), key, value, head);
            size++;
            if (size > threshold) {
                table = resize();
            }
        } else {
            node.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        if (node == null) {
            return null;
        } else {
            return node.value;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private final Node<K, V> next;
        private V value;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            return obj instanceof Map.Entry<?, ?> e
                    && Objects.equals(key, e.getKey())
                    && Objects.equals(value, e.getValue());
        }
    }

    private static int hash(Object key) {
        int hashInit;
        return (key == null) ? 0 : (hashInit = key.hashCode()) ^ (hashInit >>> 16);
    }

    private Node<K, V> getNode(K key) {
        int index = indexFor(hash(key), table.length);
        Node<K, V> node = table[index];

        while (node != null) {
            if (!Objects.equals(node.key, key)) {
                node = node.next;
            } else {
                return node;
            }
        }
        return null;
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    @SuppressWarnings("unchecked")
    private Node<K,V>[] resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * CAPACITY_MULTIPLIER;
        threshold = (int) (newCapacity * loadFactor);

        table = (Node<K, V>[]) new Node[newCapacity];

        Node<K, V> node;
        Node<K, V> nextNode;
        Node<K, V> head;

        for (Node<K, V> kvNode : oldTable) {
            if (kvNode != null) {
                node = kvNode;
                while (node != null) {
                    nextNode = node.next;
                    int index = indexFor(hash(node.key), table.length);
                    head = table[index];
                    table[index] = new Node<>(node.hash, node.key, node.value, head);
                    node = nextNode;
                }
            }
        }
        return table;
    }
}
