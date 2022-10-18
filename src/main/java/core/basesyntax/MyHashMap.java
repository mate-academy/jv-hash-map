package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int loaded;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.size = 0;
        this.loaded = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resizeTable();
        Node<K,V> newNode = new Node<>(keyHash(key), key, value, null);
        Node<K,V> bucketToPut = table[getBucketIndexByKey(key)];
        if (bucketToPut == null) {
            table[getBucketIndexByKey(key)] = newNode;
        } else {
            while (bucketToPut != null) {
                if (bucketToPut.hash == keyHash(key) && Objects.equals(bucketToPut.key, key)) {
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
        Node<K,V> bucketToFind = table[getBucketIndexByKey(key)];
        while (bucketToFind != null) {
            if (bucketToFind.hash == keyHash(key) && Objects.equals(bucketToFind.key, key)) {
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
        if (loaded == size) {
            size = 0;
            Node<K,V>[] oldTable = table;
            table = new Node[table.length << 1];
            loaded = loaded << 1;
            for (Node<K,V> nodes : oldTable) {
                while (nodes != null) {
                    put(nodes.key, nodes.value);
                    nodes = nodes.next;
                }
            }
        }
    }

    private int keyHash(K key) {
        return (key == null) ? 0 : key.hashCode() % table.length;
    }

    private int getBucketIndexByKey(K key) {
        return keyHash(key) & table.length - 1;
    }

    class Node<K,V> {
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
