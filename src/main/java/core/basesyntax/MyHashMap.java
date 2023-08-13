package core.basesyntax;

import java.util.Map;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int capacity;
    private int size;
    private Node<K, V>[] tabNode;


    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.tabNode = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int thresh = (int) (LOAD_FACTOR * capacity);
        if (this.size >= thresh) {
            tabNode = resize();
        }
        putValue(this.tabNode, key, value);
    }

    @Override
    public V getValue(K key) {
        int bucket = (key == null ? 0 : (hash(key) % capacity));
        Node<K, V> nodeBucket = tabNode[bucket];
        while (nodeBucket != null) {
            if (nodeBucket.key != null && nodeBucket.key.equals(key)
                    || nodeBucket.key == key) {
                return nodeBucket.value;
            } else {
                nodeBucket = nodeBucket.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        int oldCapacity = capacity;
        capacity = oldCapacity * 2;
        size = 0;
        Node<K, V>[] newTabNode = new Node[capacity];
        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> node = tabNode[i];
            while (node != null) {
                putValue(newTabNode, node.key, node.value);
                node = node.next;
            }
        }
        return newTabNode;
    }

    private void putValue(Node<K, V>[] tab, K key, V value) {
        int hash = hash(key);
        int bucket = (key == null ? 0 : (hash % capacity));
        if (tab[bucket] == null) {
            tab[bucket] = new Node<>(hash, key, value);
            size++;
            return;
        }
        Node<K, V> nodeBucket = tab[bucket];
        Node<K, V> predNodeBucket = null;
        boolean setted = false;
        while (!setted) {
            if (nodeBucket.key != null && nodeBucket.key.equals(key)
                    || nodeBucket.key == key) {
                nodeBucket.value = value;
                setted = true;
            } else {
                predNodeBucket = nodeBucket;
                nodeBucket = nodeBucket.next;
            }
            if (nodeBucket == null) {
                Node<K, V> newNode = new Node<>(hash, key, value);
                predNodeBucket.next = newNode;
                size++;
                setted = true;
            }
        }
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = Math.abs(key.hashCode())) ^ (h >>> 16);
    }

    private class Node<K, V> implements Map.Entry<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }

        public Node<K, V> setNext(Node<K, V> node) {
            this.next = node;
            return node;
        }

        public Node<K, V> getNext() {
            return next;
        }
    }
}
