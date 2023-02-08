package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final double CAPACITY_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] entryset = new Node[DEFAULT_CAPACITY];
    private int size;

    @Override
    public void put(K key, V value) {
        if (((double) size / entryset.length) >= CAPACITY_FACTOR) {
            resize();
        }

        int bucket = getBucket(key);
        if (entryset[bucket] == null) {
            entryset[bucket] = new Node<>(key, value);
            size++;
        } else {
            Node<K, V> entry = entryset[bucket];

            do {
                if (Objects.equals(entry.key, key)) {
                    entry.value = value;
                    return;
                }
                if (entry.next == null) {
                    break;
                }
            } while ((entry = entry.next) != null);

            entry.next = new Node<>(key, value);
            size++;
        }

    }

    @Override
    public V getValue(K key) {
        int bucket = getBucket(key);
        Node<K, V> entry = entryset[bucket];
        if (entry != null) {
            while (!Objects.equals(entry.key, key) && entry.next != null) {
                entry = entry.next;
            }
            return entry.value;
        }
        return null;
    }

    private int getBucket(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % entryset.length);
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldEntrySet = entryset;
        entryset = new Node[entryset.length * 2];
        size = 0;
        for (Node<K, V> node : oldEntrySet) {
            if (node == null) {
                continue;
            }
            do {
                put(node.key, node.value);
            } while ((node = node.next) != null);
        }
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
