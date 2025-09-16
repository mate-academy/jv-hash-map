package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = (table.length - 1) & hash;

        Node<K, V> head = table[index];

        for (Node<K, V> node = head; node != null; node = node.next) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
        }

        Node<K, V> newNode = new Node<>(hash, key, value, head);
        table[index] = newNode;
        size++;

        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = (table.length - 1) & hash;

        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * RESIZE_MULTIPLIER;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);

        for (Node<K, V> headNode : table) {
            Node<K, V> node = headNode;
            while (node != null) {
                Node<K, V> next = node.next;

                int newIndex = (newCapacity - 1) & node.hash;
                node.next = newTable[newIndex];
                newTable[newIndex] = node;

                node = next;
            }
        }
        table = newTable;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode() ^ (key.hashCode() >>> 16);
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
