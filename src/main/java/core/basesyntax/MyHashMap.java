package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    Node<K, V>[] table;
    int size;
    final float loadFactor;
    int capacity;

    public MyHashMap() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = new Node[16];
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        Node<K, V> current;
        if (searchNode(key) == null) {
            int bucket = newNode.hashCode() % DEFAULT_INITIAL_CAPACITY;
            if (table[bucket] != null) {
                Node<K, V> bucketNode = table[bucket];
                while (bucketNode.next != null) {
                        bucketNode = bucketNode.next;
                }
                bucketNode.next = newNode;
                size++;
            }
        } else {
            current = searchNode(key);
            current.value = newNode.value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> newNode = searchNode(key);
        if (newNode != null) {
            return newNode.getValue();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey()        { return key; }
        public final V getValue()      { return value; }
        public final String toString() { return key + "=" + value; }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;

            return o instanceof Map.Entry<?, ?> e
                    && Objects.equals(key, e.getKey())
                    && Objects.equals(value, e.getValue());
        }
    }

    public int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private Node searchNode(K key) {
        for (Node<K, V> node : table) {
            if (node != null) {
                do {
                    if (key == node.key || key != null && key.equals(node.key)) {
                        return node;
                    }
                    node = node.next;
                } while (node.next != null);
            }
        }
        return null;
    }
}
