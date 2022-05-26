package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] table = (Node<K,V>[])new Node[DEFAULT_CAPACITY];

    @Override

    public void put(K key, V value) {
        int threshold = (int) (table.length * LOAD_FACTOR);
        int hash = getHash(key);
        int bucket = hash % table.length;
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (threshold < size) {
            resize();
        }
        if (table[bucket] != null) {
            Node<K, V> oldNode = getNode(key, bucket);
            if (oldNode != null) {
                oldNode.value = newNode.value;
                newNode.next = oldNode.next;
                return;
            } else {
                Node<K, V> node = table[bucket];
                while (node.next != null) {
                    node = node.next;
                }
                node.next = newNode;
            }
        } else {
            table[bucket] = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        int bucket = hash % table.length;
        Node<K, V> node = getNode(key, bucket);
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
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

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
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

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[oldTable.length * 2];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
