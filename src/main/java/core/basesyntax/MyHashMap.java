package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int PRIME_NUMBER = 11;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    @Override
    public void put(K key, V value) {
        if (size == 0 && table == null) {
            threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
            table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = bucketIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
        } else if (Objects.equals(table[index].key, key)) {
            table[index].value = value;
            size--;
        } else if (table[index].next == null) {
            table[index].next = newNode;
        } else {
            Node<K, V> node = table[index];
            while (node.next != null) {
                node = node.next;
            }
            node.next = newNode;
        }
        size++;
        if (size == threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0 || table == null) {
            return null;
        }
        int index = bucketIndex(key);
        while (table[index] != null) {
            if (table[index].key == key
                    || key != null && table[index].key.equals(key)) {
                return table[index].value;
            }
            table[index] = table[index].next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final K key;
        private Node<K, V> next;
        private V value;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int bucketIndex(K key) {
        return key == null ? 0 : Math.abs((key.hashCode()
                * PRIME_NUMBER) + PRIME_NUMBER) % table.length;
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length << 1;
        threshold = threshold << 1;
        table = (Node<K, V>[]) new Node[newCapacity];
        size = 0;
        for (Node<K, V> bucket : oldTable) {
            if (bucket != null) {
                put(bucket.key, bucket.value);
                while (bucket.next != null) {
                    put(bucket.next.key, bucket.next.value);
                    bucket = bucket.next;
                }
            }
        }
    }
}
