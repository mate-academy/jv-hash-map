package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int bucket = getIndex(key, table);
        Node<K, V> current = table[bucket];
        while (current != null) {
            if (key == current.key || key != null && key.equals(current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        table[bucket] = new Node<>(hash(key), key, value, table[bucket]);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = searchNode(key);
        return (node != null) ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        int newCapacity = table.length << 1;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    public int hash(Object key) {
        return (key == null) ? 0 : (key.hashCode());
    }

    private Node<K, V> searchNode(K key) {
        int index = getIndex(key, table);
        Node<K, V> node = table[index];
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private int getIndex(K key, Node<K, V>[] table) {
        return Math.abs(hash(key) % table.length);
    }

    public static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
    }
}
