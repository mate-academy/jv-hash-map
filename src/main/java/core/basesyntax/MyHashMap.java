package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int INCREASING_NUMBER = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_SIZE = 0;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int size;
    private int threshold;

    @Override
    public void put(K key, V value) {
        int index = getHash(key);
        Node<K, V> node = table[index];
        Node<K, V> prevNode = null;

        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            } else {
                prevNode = node;
                node = node.next;
            }
        }

        Node<K, V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            prevNode.next = newNode;
        }

        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getHash(key);
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

    private int getHash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void recountThreshold() {
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    private void resize() {
        Node<K, V>[] oldTab = table;
        int newCap = oldTab == null ? 0 : oldTab.length * INCREASING_NUMBER;
        table = new Node[newCap];
        recountThreshold();
        size = DEFAULT_SIZE;
        for (Node<K, V> node : oldTab) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.hash = getHash(key);
            this.key = key;
            this.value = value;

        }
    }
}
