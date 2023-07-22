package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> node = table[index];

        if (node == null) {
            table[index] = new Node<>(key, value);
            size++;
        } else {
            while (true) {
                if (Objects.equals(key, node.key)) {
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
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : table) {
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size > threshold) {
            expand();
        }
    }

    private void expand() {
        int newCapacity = table.length * CAPACITY_MULTIPLIER;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        var oldTable = table;
        table = new Node[newCapacity];
        size = 0;

        for (var node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        int index = (key == null) ? 0 : (key.hashCode() % table.length);
        return index > 0 ? index : Math.abs(index);
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

}
