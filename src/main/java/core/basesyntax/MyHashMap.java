package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float SIZE_TO_RESIZE = 0.75f;
    private int size;
    private Node<K, V>[] table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];

    void resize() {

        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * 2];
        size = 0;

        for (Node<K, V> node : oldTable) {
            Node<K, V> current = node;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }

    }

    @Override
    public void put(K key, V value) {

        if (size >= table.length * SIZE_TO_RESIZE) {
            resize();
        }

        int index = getBucketIndex(key);
        int keyHash = (key == null) ? 0 : key.hashCode();
        Node<K, V> current = table[index];

        while (current != null) {
            if (current.hash == keyHash && Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        table[index] = new Node<>(keyHash, key, value, table[index]);
        size++;

    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        int keyHash = key == null ? 0 : key.hashCode();
        Node<K, V> current = table[index];

        while (current != null) {
            if (current.hash == keyHash && Objects.equals(key, current.key)) {
                return current.value;
            } else {
                current = current.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    int getBucketIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }
}
