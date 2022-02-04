package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = getNewTable(DEFAULT_INITIAL_CAPACITY);
        threshold = getActualThreshold();
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @Override
    public void put(K key, V value) {
        final int hashKey = hash(key);
        Node<K, V> node = table[hashKey];

        if (table[hashKey] == null) {
            table[hashKey] = new Node<>(hashKey, key, value, null);
        } else {
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(hashKey, key, value, null);
                    break;
                }
                node = node.next;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        table = getNewTable(table.length << 1);
        threshold = getActualThreshold();
        size = 0;
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        Node<K, V> node;
        for (Node<K, V> oldNode : oldTable) {
            node = oldNode;
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];
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

    private int getActualThreshold() {
        return (int) (table.length * LOAD_FACTOR);
    }

    private Node<K, V>[] getNewTable(int size) {
        return new Node[size];
    }
}
