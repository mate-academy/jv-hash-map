package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private Node<K,V>[] entryset = new Node[16];
    private double capacityFactor = 0.75;
    private int size;

    @Override
    public void put(K key, V value) {
        int bucket = key == null ? 0: key.hashCode() % entryset.length;
        if (entryset[bucket] == null) {
            entryset[bucket] = new Node<>(key, value);
            size++;
        } else {
            Node<K, V> lastEntry = entryset[bucket];
            if (key == null && lastEntry.key == null) {
                Node<K, V> insertedNode = new Node<>(key, value);
                insertedNode.next = lastEntry.next;
                return;
            } else {
                while (lastEntry.next != null) {
                    if (lastEntry.key.equals(key)) {
                        lastEntry.value = value;
                        return;
                    } else {
                        lastEntry = lastEntry.next;
                    }
                }
                lastEntry.next = new Node<>(key, value);
            }
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        return null;
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
