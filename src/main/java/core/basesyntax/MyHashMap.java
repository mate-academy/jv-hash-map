package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final int initialCapacity = 16;
    private final double loadFactor = 0.75;
    private Node<K, V>[] table = new Node[initialCapacity];
    private int size;
    private int capacity = initialCapacity;

    @Override
    public void put(K key, V value) {
        put(key, value,table);

    }

    private void put(K key, V value, Node<K, V>[] table) {
        Node<K, V> node = new Node<>(key, value, null);
        int bucketId = getBucketId(key);
        Node<K, V> nodeTemp = table[bucketId];
        if (nodeTemp == null) {
            table[bucketId] = node;
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
        if (++size >= capacity * loadFactor) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int bucket = getBucketId(key);
        Node<K, V> nodeTemp = table[bucket];
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

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;

        }
    }

    private void resize() {
        capacity *= 2;
        size = 0;
        Node<K, V>[] nodes = new Node[capacity];
        for (Node element : table) {
            Node<K, V> nodeTemp = element;
            while (nodeTemp != null) {
                put(nodeTemp.key, nodeTemp.value,nodes);
                nodeTemp = nodeTemp.next;
            }
        }
        table = nodes;
    }

    private int getBucketId(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

}
