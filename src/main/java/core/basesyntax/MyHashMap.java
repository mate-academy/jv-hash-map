package core.basesyntax;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private List<Node<K, V>>[] table;
    private int size;

    public MyHashMap() {
        table = new ArrayList[DEFAULT_CAPACITY];
        for (int i = 0; i < DEFAULT_CAPACITY; ++i) {
            table[i] = new ArrayList<>();
        }
    }

    @Override
    public void put(K key, V value) {
        int hash = (key != null) ? key.hashCode() : 0;
        int insertIndex = Math.abs(hash) % table.length;

        List<Node<K, V>> bucket = table[insertIndex];
        if (bucket == null) {
            bucket = new ArrayList<>();
            table[insertIndex] = bucket;
        }

        for (Node<K, V> node : bucket) {
            if ((node.key == null && key == null) || (node.key != null && node.key.equals(key))) {
                node.value = value;
                return;
            }
        }

        bucket.add(0, new Node<>(hash, key, value));
        size++;
        if ((double) size / table.length > DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = (key != null) ? key.hashCode() : 0;
        int index = Math.abs(hash) % table.length;

        List<Node<K, V>> bucket = table[index];

        if (bucket != null) {
            for (Node<K, V> node : bucket) {
                if ((node.key == null && key == null)
                        || (node.key != null && node.key.equals(key))) {
                    return node.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = table.length << 1;
        List<Node<K, V>>[] newTable = new ArrayList[newCapacity];

        for (List<Node<K, V>> nodes : table) {
            if (nodes != null) {
                for (Node<K, V> node : nodes) {
                    int newHash = node.hash;
                    int newIndex = Math.abs(newHash) % newCapacity;

                    if (newTable[newIndex] == null) {
                        newTable[newIndex] = new ArrayList<>();
                    }
                    newTable[newIndex].add(0, node);
                }
            }
        }
        table = newTable;
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
