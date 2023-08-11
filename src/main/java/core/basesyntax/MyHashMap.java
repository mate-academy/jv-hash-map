package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int DEFAULT_CAPACITY = 16; // aka 16
    static final int RESIZE_COEFFICIENT = 2;
    static final float LOAD_FACTOR = 0.75F;

    private int size;
    private int capacity = DEFAULT_CAPACITY;

    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        if (getThreshold() == size) {
            resize();
        }

        int index = getIndex(key);
        if (table[index] != null) {
            Node<K, V> node = table[index];
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            newNode.next = table[index];
        }
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
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

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * RESIZE_COEFFICIENT];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getThreshold() {
        return (int) (table.length * LOAD_FACTOR);
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
