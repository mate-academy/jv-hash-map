package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (!isEnoughSpace()) {
            resize();
        }
        putValue(getIndex(key), new Node<>(key, value, null));
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
        if (node != null) {
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

    private boolean isEnoughSpace() {
        return size < threshold;
    }

    private void resize() {
        int newCapacity = calculateCapacity();
        updateThreshold(newCapacity);
        size = 0;
        Node<K, V>[] data = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> node : data) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void putValue(int index, Node<K, V> newNode) {
        Node<K, V> current = table[index];
        Node<K, V> prior = null;
        if (current == null) {
            table[index] = newNode;
        } else {
            while (current != null) {
                if (Objects.equals(newNode.key, current.key)) {
                    current.value = newNode.value;
                    return;
                }
                prior = current;
                current = current.next;
            }
            prior.next = newNode;
        }
        size++;
    }

    private int getIndex(K key) {
        return hash(key) % table.length;
    }

    private int hash(K key) {
        int hash;
        return (key == null) ? 0 : Math.abs(hash = key.hashCode()) ^ (hash >>> 16);
    }

    private int calculateCapacity() {
        return table.length << 1;
    }

    private void updateThreshold(int capacity) {
        threshold = (int) (DEFAULT_LOAD_FACTOR * capacity);
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
}
