package core.basesyntax;

import java.util.LinkedList;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private Node<K,V>[] entryset = new Node[16];
    private double capacityFactor = 0.75;
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
    }

    @Override
    public V getValue(K key) {
        int bucket = getBucket(key);
        Node<K, V> entry = entryset[bucket];
        if (entryset[bucket] != null) {
            while (!Objects.equals(entry.key, key) && entry.next != null) {
                entry = entry.next;
            }
            return entry.value;
        }
        return null;
    }

    private int getBucket(K key) {
        return key == null ? 0: Math.abs(key.hashCode() % entryset.length);
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {

    }

    private static class Node<K,V> {
        Node<K,V> next;
        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
