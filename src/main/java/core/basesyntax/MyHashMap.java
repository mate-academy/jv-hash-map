package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resizeTable();
        int hash = keyHash(key);
        Node<K,V> newNode = new Node<>(hash, key, value, null);
        Node<K,V> bucketToPut = table[hash];
        if (bucketToPut == null) {
            table[hash] = newNode;
        } else {
            while (bucketToPut != null) {
                if (Objects.equals(bucketToPut.key, key)) {
                    bucketToPut.value = value;
                    return;
                }
                if (bucketToPut.next == null) {
                    bucketToPut.next = newNode;
                    break;
                }
                bucketToPut = bucketToPut.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = keyHash(key);
        Node<K,V> bucketToFind = table[hash];
        while (bucketToFind != null) {
            if (Objects.equals(bucketToFind.key, key)) {
                return bucketToFind.value;
            }
            bucketToFind = bucketToFind.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeTable() {
        if (threshold == size) {
            size = 0;
            Node<K,V>[] oldTable = table;
            table = new Node[table.length << 1];
            threshold = threshold << 1;
            for (Node<K,V> nodes : oldTable) {
                while (nodes != null) {
                    put(nodes.key, nodes.value);
                    nodes = nodes.next;
                }
            }
        }
    }

    private int keyHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private class Node<K,V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
