package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        private boolean hasNext() {
            return next != null;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 >= threshold) {
            resize();
        }
        if (!isKeyHashPresent(key)) {
            table[hash(key)] = new Node<>(hash(key), key, value, null);
            size++;
        } else if (getNode(key) == null) {
            linkCollision(new Node<>(hash(key), key, value, null));
        } else {
            getNode(key).value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> target;
        return (target = getNode(key)) != null ? target.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K,V> getNode(K key) {
        if (isKeyHashPresent(key)) {
            for (Node<K, V> bucket = table[hash(key)]; bucket != null; bucket = bucket.next) {
                if (Objects.equals(bucket.key, key)) {
                    return bucket;
                }
            }
        }
        return null;
    }

    private boolean isKeyHashPresent(K key) {
        return table[hash(key)] != null;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void linkCollision(Node<K, V> nodeToLink) {
        Node<K, V> bucket = table[nodeToLink.hash];
        while (bucket.hasNext()) {
            bucket = bucket.next;
        }
        bucket.next = nodeToLink;
        size++;
    }

    private void resize() {
        final Node<K, V>[] temp = table;
        table = (Node<K,V>[]) new Node[table.length << 1];
        threshold = threshold << 1;
        size = 0;
        copyTabContent(temp);
    }

    private void copyTabContent(Node<K, V>[] donorTable) {
        for (Node<K, V> bucket : donorTable) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }
}
