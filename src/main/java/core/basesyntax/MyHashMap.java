package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;

    private Node<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int hash = getHash(key);
        int index = getBucketIndex(hash);

        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash
                    && (Objects.equals(key, current.key))) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        table[index] = new Node<>(hash, key, value, table[index]);
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        int index = getBucketIndex(hash);

        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash
                    && Objects.equals(key, current.key)) {
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

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getHash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getBucketIndex(int hash) {
        return Math.floorMod(hash, table.length);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * CAPACITY_MULTIPLIER;
        table = new Node[newCapacity];
        size = 0;

        for (Node<K, V> head : oldTable) {
            Node<K, V> current = head;
            while (current != null) {
                int newIndex = getBucketIndex(current.hash);

                final Node<K, V> nextNode = current.next;

                current.next = table[newIndex];
                table[newIndex] = current;

                size++;
                current = nextNode;

            }
        }
    }
}
