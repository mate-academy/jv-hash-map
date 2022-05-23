package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int bucket;
    private int size;
    private Node<K, V>[] table = (Node<K,V>[])new Node[DEFAULT_CAPACITY];

    @Override
    public void put(K key, V value) {
        int threshold = (int) (table.length * LOAD_FACTOR);
        int hash = Math.abs(hashCode(key));
        Node<K, V> node = new Node<>(hash, key, value, null);
        if (threshold >= size) {
            bucket = getBucket(key, hash);
            if (table[bucket] == null) {
                table[bucket] = node;
            } else {
                Node<K, V> oldNode = getNode(key, bucket);
                if (oldNode != null) {
                    oldNode.value = node.value;
                    node.next = oldNode.next;
                    return;
                } else {
                    Node<K, V> secondLastNode = table[bucket];
                    while (secondLastNode.next != null) {
                        secondLastNode = secondLastNode.next;
                    }
                    secondLastNode.next = node;
                }
            }
            size++;
        } else {
            table = resize();
            put(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        int hash = Math.abs(hashCode(key));
        bucket = getBucket(key, hash);
        Node<K, V> node = getNode(key, bucket);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int hashCode(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.value = value;
            this.key = key;
            this.next = next;
        }
    }

    private int getBucket(K key, int hash) {
        if (key == null) {
            bucket = 0;
        } else {
            bucket = hash % table.length;
        }
        return bucket;
    }

    private Node<K, V> getNode(K key, int bucket) {
        Node<K, V> node = table[bucket];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[oldTable.length * 2];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        return table;
    }
}
