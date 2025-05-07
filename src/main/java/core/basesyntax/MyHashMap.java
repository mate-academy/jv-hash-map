package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int size = 0;

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> oldNode = table[getIndex(key)];
        if (table[getIndex(key)] == null) {
            table[getIndex(key)] = newNode;
            size++;
        } else {
            while (oldNode != null) {
                if (isEqual(oldNode.key, key)) {
                    oldNode.value = value;
                    return;
                }
                if (oldNode.next == null) {
                    oldNode.next = newNode;
                    size++;
                    return;
                }
                oldNode = oldNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
        if (node == null) {
            return null;
        }
        for (node = table[getIndex(key)]; node != null; node = node.next) {
            if (isEqual(key, node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private boolean isEmpty() {
        return size == 0;
    }

    private boolean isEqual(Object first, Object second) {
        return Objects.equals(first, second);
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] oldTable = table;
        size = 0;
        table = new Node[newCapacity];

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }
}
