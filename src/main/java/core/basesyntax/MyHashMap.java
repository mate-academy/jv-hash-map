package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DOUBLE_VALUE = 2;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int capacity;
    private float threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = (key == null) ? 0 : getIndex(key);
        Node<K, V> bucket = new Node<>((key == null) ? 0 : key.hashCode(), key, value);

        if (table[index] == null) {
            table[index] = bucket;
            size++;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            bucket.next = table[index];
            table[index] = bucket;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            Node<K, V> bucket = table[0];
            while (bucket != null) {
                if (bucket.key == null) {
                    return bucket.value;
                }
                bucket = bucket.next;
            }
            return null;
        }

        int index = getIndex(key);
        Node<K, V> bucket = table[index];

        while (bucket != null) {
            if (Objects.equals(bucket.key, key)) {
                return bucket.value;
            }
            bucket = bucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        int index = key.hashCode() % capacity;
        return index < 0 ? -index : index;
    }

    private void resize() {
        int newCapacity = table.length * DOUBLE_VALUE;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        capacity = newCapacity;
        size = 0;
        Node<K,V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[newCapacity];

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
