package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

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
        resize();
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
        int hash = key == null ? 0 : (31 * 17 + Math.abs(key.hashCode()));
        Node<K, V> bucket = table[hash % table.length];
        if (bucket != null && Objects.equals(bucket.key, key)) {
            return bucket.value;
        } else if (bucket != null && bucket.next != null) {;
            while (bucket.next != null) {
                bucket = bucket.next;
                if (Objects.equals(bucket.key, key)) {
                    return bucket.value;
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
        while (current.next != null || Objects.equals(current.key, key)) {
            if (Objects.equals(current.key, key)) {
                current.value = node.value;
                return;
            }
            current = current.next;
        }
        current.next = node;
        size++;
    }

    private void resize() {
        if (size >= threshold) {
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * 2];
            size = 0;
            for (Node<K, V> node : oldTable) {
                if (node != null) {
                    put(node.key, node.value);
                    while (node.next != null) {
                        put(node.next.key, node.next.value);
                        node = node.next;
                    }
                }

            }
            threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        }
    }




}
