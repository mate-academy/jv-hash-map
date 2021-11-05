package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * table.length);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = Math.abs(getHashCode(key)) % table.length;
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        Node<K, V> node = table[index];
        Node<K, V> oldNode = node;
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            oldNode = node;
            node = node.next;
        }
        oldNode.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(getHashCode(key)) % table.length;
        Node<K, V> item = table[index];
        while (item != null) {
            if (Objects.equals(key, item.key)) {
                return item.value;
            }
            item = item.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        int capacity = table.length * 2;
        Node<K, V>[] reserveTable = table;
        table = new Node[capacity];
        threshold = (int) (LOAD_FACTOR * capacity);
        for (Node<K, V> item: reserveTable) {
            while (item != null) {
                put(item.key, item.value);
                item = item.next;
            }
        }
    }

    private int getHashCode(K key) {
        return 31 * ((key == null) ? 0 : key.hashCode());
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
