package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final double CAPACITY_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] entryset;
    private int size;

    public MyHashMap() {
        this.entryset = new Node[DEFAULT_CAPACITY];
    }

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

            while (entry != null) {
                if (Objects.equals(entry.key, key)) {
                    entry.value = value;
                    return;
                }
                entry = entry.next;
            }

            entryset[bucket] = new Node<>(key, value, entryset[bucket]);
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
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
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

        public Node(K key, V value, Node<K, V> node) {
            this.key = key;
            this.value = value;
            this.next = node;
        }
    }
}
