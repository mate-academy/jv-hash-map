package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private static final int DEFAULT_SIZE_OF_ARRAY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int INCREASING_T = 2;
    private Node<K, V> [] table = new Node[DEFAULT_SIZE_OF_ARRAY];
    private Node<K, V> node;
    private int size;
    private int threshold;

    @Override
    public void put(K key, V value) {
        Node<K, V> exsitNode = getNode(key);
        if (exsitNode != null) {
            exsitNode.value = value;
            return;
        }
        checkSize();
        int index = getIndexOfArray(key);
        Node<K, V> node = new Node<>(key, value);
        if (table[index] != null) {
            node.next = table[index];
        }
        table[index] = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    public int getIndexOfArray(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    public void checkSize() {
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    public Node<K, V> getNode(K key) {
        int index = getIndexOfArray(key);
        Node<K, V> node = table[index];
        if (node != null) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
            while (node.next != null) {
                node = node.next;
                if (Objects.equals(node.key, key)) {
                    return node;
                }
            }
        }
        return null;
    }

    public void resize() {
        int newSizeOfHashMap = table.length * INCREASING_T;
        Node<K, V>[] oldValue = table;
        table = new Node[newSizeOfHashMap];
        size = 0;
        for (int i = 0; i < oldValue.length; i++) {
            Node<K, V> node = oldValue[i];
            if (node != null) {
                put(node.key, node.value);
                while (node.next != null) {
                    node = node.next;
                    put(node.key, node.value);
                }
            }
        }
    }
}
