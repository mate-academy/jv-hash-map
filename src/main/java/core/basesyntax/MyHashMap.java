package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int currentCapacity;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.currentCapacity = DEFAULT_CAPACITY;
        this.threshold = (int)(DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        ensureCapacity();
        Node<K, V> node = new Node<>(key, value, null);
        int index = getBucketIndex(key);
        if (table[index] == null) {
            table[index] = node;
            size++;
            return;
        }
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = node;
                size++;
            }
            current = current.next;
        }
    }



    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getBucketIndex(key)];
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
        return 0;
    }

    private void ensureCapacity() {
        if (size == currentCapacity * DEFAULT_LOAD_FACTOR) {
            size = 0;
            currentCapacity *= 2;
            Node<K,V>[] oldTable = table;
            table = new Node[currentCapacity];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getBucketIndex(K key) {
        if (getHash(key) == 0) {
            return 0;
        }
        return Math.abs(getHash(key) % table.length);
    }

    private int getHash(K key) {
        int h;
        return (key == null) ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> 16));
    }

    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            value = value;
            this.next = next;
        }

    }
}
