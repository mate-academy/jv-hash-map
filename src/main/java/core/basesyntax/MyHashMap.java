package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.4;
    private double loadFactor;
    private int capacity;
    Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        loadFactor = DEFAULT_LOAD_FACTOR;
        capacity = DEFAULT_CAPACITY;
        table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    public MyHashMap(int capacity, double loadFactor) {
        this.loadFactor = loadFactor;
        this.capacity = capacity;
        this.table = (Node<K,V>[]) new Node[capacity];
        this.size = 0;
    }

    private int indexFor(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        size = 0;
        capacity = capacity * 2;
        Node<K, V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[capacity];
        transfer(oldTable);
    }

    public void transfer(Node<K, V>[] oldTable) {
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                put(oldTable[i].key, oldTable[i].value);
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= capacity * loadFactor) {
            resize();
        }
        int index = indexFor(key);
        if (table[index] == null) {
            table[index] = new Node<K, V>(key, value);
            size++;
            return;
        }
        while (table[index] != null) {
            if (Objects.equals(key, table[index].key)) {
                table[index].value = value;
                return;
            }
            index++;
            if (index == capacity) {
                index = 0;
            }
        }
        table[index] = new Node<K, V>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = indexFor(key);
        while (table[index] != null) {
            if (Objects.equals(key, table[index].key)) {
                return table[index].value;
            }
            index++;
            if (index == capacity) {
                index = 0;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
