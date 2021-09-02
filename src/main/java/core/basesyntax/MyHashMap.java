package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;
    private int capacity = DEFAULT_INITIAL_CAPACITY;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            hash = hashCode(key);
            this.key = key;
            this.value = value;
            this.next = next;
        }

        private int hashCode(K key) {
            return key == null ? 0 : (31 * 17 + Math.abs(key.hashCode()));
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = newNode.hash % table.length;
        if (table[index] != null) {
            findLastNext(index, key, newNode);
        } else {
            table[index] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> bucket: table) {
            if (bucket != null && Objects.equals(bucket.key, key)) {
                return bucket.value;
            } else if (bucket != null && bucket.next != null) {
                Node<K, V> prev;
                while (bucket.next != null) {
                    prev = bucket;
                    bucket = bucket.next;
                    if (Objects.equals(bucket.key, key)) {
                        return bucket.value;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void findLastNext(int index, K key, Node<K, V> node) {
        Node<K, V> current = table[index];
        Node<K, V> prev;
        while (current.next != null || Objects.equals(current.key, key)) {
            if (Objects.equals(current.key, key)) {
                table[index] = node;
                return;
            }
            prev = current;
            current = current.next;
        }
        current.next = node;
        size++;
    }

    /*private Node<K, V>[] resize() {
        return null;
    }

    private void transfer() {

    }*/
}
