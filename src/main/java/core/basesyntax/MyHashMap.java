package core.basesyntax;

import java.util.Objects;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int threshold;
    private int capacity;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        this.capacity = DEFAULT_CAPACITY;
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int insertIndex = findElementIndex(key);
        Node<K, V> node = table[insertIndex];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        table[insertIndex] = new Node<>(key, value, table[insertIndex]);
        size++;
    }

    @Override
    public V getValue(K key) {
        int nodeIndex = findElementIndex(key);
        Node<K, V> node = table[nodeIndex];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    private int findElementIndex(K key) {
        return key == null ? 0 : Math.abs(getHash(key) % capacity);
    }

    private int getHash(K key) {
        return key.hashCode();
    }

    private void resize() {
        if (size >= threshold) {
            size = 0;
            capacity *= 2;
            threshold = (int) (LOAD_FACTOR * capacity);
            Node<K, V>[] oldTable = table;
            table = new Node[capacity];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
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
