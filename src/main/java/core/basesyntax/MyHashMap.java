package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_CAPACITY_MULTIPLIER = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int index = getBucketIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        Node<K, V> newNode = new Node<>(key, value);
        put(newNode, index);
        size++;
    }

    private void put(Node<K, V> node, int index) {
        if (table[index] == null) {
            table[index] = node;
        } else {
            Node<K, V> current = table[index];
            while (current.next != null) {
                current = current.next;
            }
            current.next = node;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        Node<K, V> node = table[index];
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
        return size;
    }

    private void resizeIfNeeded() {
        int threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        if (size >= threshold) {
            resize();
        }
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * DEFAULT_CAPACITY_MULTIPLIER];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        MyHashMap<Integer, Integer> map = new MyHashMap<>();
        map.put(17, 1);
        map.put(33, 2);
    }
}
