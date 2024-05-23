package core.basesyntax;

import java.util.Map;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private float loadFactor;

    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && (node.key == key || key != null && key.equals(node.key))) {
                node.value = value;
                return;
            }
        }
        addNode(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && (node.key == key || key != null && key.equals(node.key))) {
                return node.value;
            }
        }
        return null;
    }

    private void addNode(int hash, K key, V value, int bucketIndex) {
        Node<K, V> node = table[bucketIndex];
        table[bucketIndex] = new Node<>(hash, key, value, node);
        if (++size > threshold) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    private void transfer(Node<K, V>[] newTable) {
        Node<K, V>[] oldTable = table;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = indexFor(node.hash, newTable.length);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode() ^ (key.hashCode() >>> 16);
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
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
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
    }
}
