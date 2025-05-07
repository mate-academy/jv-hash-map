package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int RESIZE_VALUE = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        Node<K, V> bucket = table[getIndexByHash(key)];
        Node<K, V> newNode = new Node<>(key, value, null);
        if (bucket == null) {
            table[getIndexByHash(key)] = newNode;
        }
        while (bucket != null) {
            if (Objects.equals(key, bucket.key)) {
                bucket.value = value;
                return;
            }
            if (bucket.next == null) {
                bucket.next = newNode;
                break;
            }
            bucket = bucket.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByHash(key);
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

    private int getIndexByHash(K key) {
        return key == null ? 0 : Math.abs(Objects.hash(key) % table.length);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] prevTable = table;
        table = new Node[prevTable.length * RESIZE_VALUE];
        for (Node<K, V> node : prevTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
