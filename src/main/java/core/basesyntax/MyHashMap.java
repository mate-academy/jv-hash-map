package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final double CAPACITY_FACTOR = 0.75;
    private Node<K, V>[] entryset = new Node[16];
    private int size;

    @Override
    public void put(K key, V value) {
        int bucket = getBucket(key);
        if (entryset[bucket] == null) {
            entryset[bucket] = new Node<>(key, value);
            size++;
        } else {
            Node<K, V> entry = entryset[bucket];

            if (Objects.equals(entry.key, key)) {
                entry.value = value;
                return;
            }

            while (entry.next != null) {
                entry = entry.next;
                if (Objects.equals(entry.key, key)) {
                    entry.value = value;
                    return;
                }
            }

            entry.next = new Node<>(key, value);
            size++;
        }
        if (((double) size / entryset.length) > CAPACITY_FACTOR) {
            resize();
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

    private int getBucket(K key, int capacity) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] newEntrySet = new Node[entryset.length * 2];
        for (Node<K, V> node : entryset) {
            if (node == null) {
                continue;
            }
            while (node != null) {
                int bucket = getBucket(node.key, newEntrySet.length);

                if (newEntrySet[bucket] == null) {
                    newEntrySet[bucket] = new Node<>(node.key, node.value);
                } else {
                    Node<K, V> entry = newEntrySet[bucket];

                    while (entry.next != null) {
                        entry = entry.next;
                    }

                    entry.next = new Node<>(node.key, node.value);
                }
                node = node.next;
            }
        }
        entryset = newEntrySet;
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
