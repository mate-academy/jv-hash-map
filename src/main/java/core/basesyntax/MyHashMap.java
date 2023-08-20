package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] tables;
    private int threshold;
    private int size;

    public MyHashMap() {
        tables = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) LOAD_FACTOR * DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        int nodeHash = hash(key);
        int index = getBucketIndex(nodeHash);
        putValue(index, new Node<>(nodeHash, key, value, null));
        if (isThresholdReached()) {
            resizeTables();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = tables[getBucketIndex(hash(key))];
        if (current == null) {
            return null;
        }
        do {
            if (current.equalsKey(key)) {
                return current.value;
            }
        } while ((current = current.next) != null);
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putValue(int index, Node<K, V> node) {
        if (tables[index] == null) {
            tables[index] = node;
        } else {
            Node<K, V> current = tables[index];
            while (true) {
                if (replaceValue(current, node)) {
                    return;
                }
                if (!current.hasNext()) {
                    current.next = node;
                    break;
                }
                current = current.next;
            }
        }
        size++;
    }

    private boolean replaceValue(Node<K, V> current, Node<K, V> replacement) {
        if (current.equalsKey(replacement.key)) {
            current.value = replacement.value;
            return true;
        }
        return false;
    }

    private boolean isThresholdReached() {
        return size >= threshold;
    }

    private void resizeTables() {
        final Node<K, V>[] temp = tables;
        tables = (Node<K, V>[]) new Node[tables.length << 1];
        size = 0;
        updateThreshold();
        for (Node<K, V> node : temp) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getBucketIndex(int hashCode) {
        return hashCode % tables.length;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void updateThreshold() {
        threshold = (int) (LOAD_FACTOR * tables.length);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        private boolean equalsKey(K another) {
            return Objects.equals(key, another);
        }

        private boolean hasNext() {
            return next != null;
        }
    }
}
