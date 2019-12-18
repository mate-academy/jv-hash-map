package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_BUCKETS = 16;

    private Node[] table;
    private int capacity;
    private int size;

    public MyHashMap() {
        this.capacity = 0;
        this.size = 0;
        this.table = new Node[DEFAULT_BUCKETS];
    }

    private int getHash(K key) {
        if (key == null) {
            return 0;
        }
        int k = Math.abs(key.hashCode());
        k ^= (k >>> 20) ^ (k >>> 12);
        return k ^ (k >>> 7) ^ (k >>> 4);
    }

    private int getIndex(int hash) {
        return hash % table.length;
    }

    private void checkCapacity() {
        if (capacity >= table.length * LOAD_FACTOR) {
            grow();
        }
    }

    private boolean checkBucketValue(int index) {
        return table[index] != null;
    }

    private void grow() {
        size = 0;
        capacity = 0;
        Node[] oldTable = table;
        table = new Node[table.length << 1];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.nextNode;
            }
        }
    }

    @Override
    public void put(K key, V value) {
        checkCapacity();
        int hash = getHash(key);
        int index = getIndex(hash);
        if (checkBucketValue(index)) {
            Node<K, V> node = table[index];
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                node = node.nextNode;
            }
            node = table[index];
            table[index] = new Node<K, V>(hash, key, value, node);
            size++;
            return;
        }
        table[index] = new Node<K, V>(hash, key, value);
        capacity++;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(getHash(key));
        if (checkBucketValue(index)) {
            Node<K, V> node = table[index];
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    return node.value;
                }
                node = node.nextNode;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> nextNode;

        private Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.nextNode = null;
        }

        private Node(int hash, K key, V value, Node<K, V> nextNode) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }
}
