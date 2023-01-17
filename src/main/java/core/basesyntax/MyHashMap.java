package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold && size <= MAXIMUM_CAPACITY) {
            resize();
        }
        putVal(hash(key), key, value);
    }

    private void putVal(int hash, K key, V value) {
        int bucket = getBucket(key);
        Node<K, V> newNode = new Node<>(hash, key, value);
        if (table[bucket] == null) {
            table[bucket] = newNode;
            size++;
        } else {
            Node<K, V> currentNode = table[bucket];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                } else if (currentNode.next == null) {
                    currentNode.next = newNode;
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length + DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] table) {
        size = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                Node<K, V> movingNode = table[i];
                while (movingNode != null) {
                    putVal(movingNode.hash, movingNode.key, movingNode.value);
                    movingNode = movingNode.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (table[getBucket(key)] != null) {
            Node<K, V> kvNode = table[getBucket(key)];
            while (kvNode != null) {
                if (Objects.equals(kvNode.key, key)) {
                    return kvNode.value;
                }
                kvNode = kvNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucket(K key) {
        return Math.abs(hash(key) % table.length);
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
