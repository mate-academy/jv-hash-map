package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.size = 0;
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
        int bucket = findBucket(hash(key));
        Node<K, V> node = table[bucket];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.nextNode;
        }
        Node<K, V> newNode = new Node<>(key, value, table[bucket]);
        table[bucket] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucket = findBucket(hash(key));
        if (table[bucket] == null) {
            return null;
        }
        Node<K, V> node = table[bucket];
        do {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.nextNode;
        } while (node != null);
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity  = table.length * 2;
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.nextNode;
            }
        }
    }

    private int hash(K key) {
        int hash;
        return (key == null) ? 0 : Math.abs((hash = key.hashCode()) ^ (hash >>> 16));
    }

    private int findBucket(int hash) {
        return hash & (table.length - 1);
    }

    static class Node<K, V> {
        public final K key;
        public V value;
        public Node<K, V> nextNode;

        private Node(K key, V value, Node<K, V> nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }
}
