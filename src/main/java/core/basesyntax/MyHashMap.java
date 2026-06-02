package core.basesyntax;

import java.util.Arrays;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int HASH_MASK = Integer.MAX_VALUE;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof MyHashMap<?, ?> myHashMap)) {
            return false;
        }

        return getSize() == myHashMap.getSize()
                && threshold == myHashMap.threshold
                && Arrays.equals(table, myHashMap.table);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getSize(), threshold);
        result = 31 * result + Arrays.hashCode(table);
        return result;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int hash = hash(key);
        int index = (hash & HASH_MASK) % table.length;
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash && Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = (hash & HASH_MASK) % table.length;
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash && Objects.equals(current.key, key)) {
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

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = (node.hash & HASH_MASK) % newCapacity;
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private class Node<K, V> {
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
