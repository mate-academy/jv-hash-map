package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        int bucketIndex = getBucketIndex(key);
        int hashCode = hash(key);
        if (table[bucketIndex] == null) {
            table[bucketIndex] = new Node<>(hashCode, key, value, null);
            size++;
        } else {
            Node<K, V> current = table[bucketIndex];
            while (current != null) {
                if (current.key == null && key == null
                        || key != null && key.equals(current.key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            table[bucketIndex] = new Node<>(hashCode, key, value, table[bucketIndex]);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> current = table[bucketIndex];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
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
        Node<K,V>[] oldTable = table;
        int newCapacity = table.length * GROW_FACTOR;
        Node<K, V>[] newTable = new Node[newCapacity];
        table = newTable;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                int index = (node.hash & Integer.MAX_VALUE) % newCapacity;
                Node<K, V> next = node.next;
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
    }

    private int hash(K key) {
        return Math.abs(Objects.hashCode(key));
    }

    private int getBucketIndex(K key) {
        return hash(key) % table.length;
    }

    private static class Node<K,V> {
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
