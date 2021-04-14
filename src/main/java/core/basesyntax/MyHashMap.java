package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_SIZE_INCREASE = 2;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    private int size;

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        Node<K, V> node = table[findIndex(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value);
                size++;
                return;
            }
            node = node.next;
        }
        table[findIndex(key)] = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[findIndex(key)];
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

    private void resize() {
        size = 0;
        threshold *= DEFAULT_SIZE_INCREASE;
        Node<K, V>[] previousTable = table;
        table = new Node[table.length * DEFAULT_SIZE_INCREASE];
        for (Node<K, V> node : previousTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int findIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
