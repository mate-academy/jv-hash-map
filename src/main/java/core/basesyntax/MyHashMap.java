package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    static final float LOAD_FACTOR = 0.75f;
    static final int DEFAULT_BUCKETS = 16;

    private Node[] table;
    private int buckets;
    private int capacity;
    private int size;

    public MyHashMap(int buckets) {
        this.buckets = buckets;
        this.capacity = 0;
        this.size = 0;
        this.table = new Node[buckets];
    }

    public MyHashMap() {
        this.buckets = DEFAULT_BUCKETS;
        this.capacity = 0;
        this.size = 0;
        this.table = new Node[DEFAULT_BUCKETS];
    }

    private int getHash(K key) {
        int k = Math.abs(key.hashCode());
        k ^= (k >>> 20) ^ (k >>> 12);
        return (key == null) ? 0 : k ^ (k >>> 7) ^ (k >>> 4);
    }

    private int getIndex(int hash, int tableLength) {
        int i = hash % tableLength;
        return i;
    }

    private void checkCapacity() {
        if (capacity >= buckets * LOAD_FACTOR) {
            grow();
        }
    }

    private boolean checkBucketValue(int index) {
        return table[index] != null;
    }

    private void grow() {
        size = 0;
        capacity = 0;
        buckets *= 2;
        Node[] oldTable = table;
        table = new Node[buckets];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.nextNode;
            }
        }
    }

    private void putForNull(V value) {
        if (checkBucketValue(0)) {
            Node<K, V> node = table[0];
            while (node != null) {
                if (node.key == null) {
                    node.value = value;
                    return;
                }
                node = node.nextNode;
            }
            node = table[0];
            table[0] = new Node<K, V>(0, null, value, node);
            size++;
            return;
        }
        table[0] = new Node<K, V>(0, null, value);
        capacity++;
        size++;
    }

    private V getForNull() {
        if (checkBucketValue(0)) {
            Node<K, V> node = table[0];
            while (node != null) {
                if (node.key == null) {
                    return node.value;
                }
                node = node.nextNode;
            }
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        checkCapacity();
        if (key == null) {
            putForNull(value);
            return;
        }
        int hash = getHash(key);
        int index = getIndex(hash, buckets);
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
        if (key == null) {
            return getForNull();
        }
        int hash = getHash(key);
        int index = getIndex(hash, buckets);
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
