package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] values = new Node[INITIAL_CAPACITY];
    private int size;

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        int bucketId = getBucketId(key);
        Node<K, V> nodeTemp = values[bucketId];
        if (nodeTemp == null) {
            values[bucketId] = node;
            size++;
            return;
        }
        while (nodeTemp != null) {
            if (Objects.equals(nodeTemp.key,key)) {
                nodeTemp.value = value;
                return;
            }
            if (nodeTemp.next == null) {
                break;
            }
            nodeTemp = nodeTemp.next;
        }
        nodeTemp.next = node;
        if (++size >= values.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getBucketId(key);
        Node<K, V> nodeTemp = values[index];
        while (nodeTemp != null) {
            if (Objects.equals(nodeTemp.key,key)) {
                return nodeTemp.value;
            }
            nodeTemp = nodeTemp.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldBuckets = values;
        values = new Node[values.length * 2];
        for (Node element : oldBuckets) {
            Node<K, V> nodeTemp = element;
            while (nodeTemp != null) {
                put(nodeTemp.key, nodeTemp.value);
                nodeTemp = nodeTemp.next;
            }
        }
    }

    private int getBucketId(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % values.length);
    }
}
