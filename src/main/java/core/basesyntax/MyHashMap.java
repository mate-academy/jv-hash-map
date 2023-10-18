package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLY = 2;

    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
        } else {
            putForNonNullKey(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }
        int index = getIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        int index = (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
        return index;
    }

    private void resize() {
        if (size > threshold) {
            expand();
        }
    }

    private void expand() {
        int newCapacity = table.length * CAPACITY_MULTIPLY;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void putForNullKey(V value) {
        int index = 0;
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.key == null) {
                node.value = value;
                return;
            }
        }
        Node<K, V> newNode = new Node<>(null, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
        resize();
    }

    private void putForNonNullKey(K key, V value) {
        int index = getIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
        resize();
    }

    private V getForNullKey() {
        for (Node<K, V> node = table[0]; node != null; node = node.next) {
            if (node.key == null) {
                return node.value;
            }
        }
        return null;
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
