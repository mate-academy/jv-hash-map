package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private Node<K,V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.size = 0;
        this.threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        for (Node<K, V> element = table[getIndex(key)]; element != null; element = element.next) {
            if ((element.hash == hash(key)) && ((element.key == null && key == null)
                    || (element.key != null && element.key.equals(key)))) {
                element.value = value;
                return;
            }
        }

        Node<K, V> newNode = table[getIndex(key)];
        table[getIndex(key)] = new Node<>(hash(key), key, value, newNode);
        size++;
        if (size > threshold) {
            resize();
        }
    }

    public V getValue(K key) {
        if (table != null) {
            for (Node<K, V> element = table[getIndex(key)]; element != null; element = element.next) {
                if ((element.hash == hash(key)) && (Objects.equals(element.key, key))) {
                    return element.value;
                }
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K,V>[] resize() {
        int oldCapacity = (table == null) ? 0 : table.length;
        int newCapacity = (oldCapacity > 0) ? oldCapacity << 1 : DEFAULT_CAPACITY;
        int newThreshold = (threshold > 0) ? threshold << 1 : (int) (DEFAULT_LOAD_FACTOR * newCapacity);

        if (newThreshold == 0) {
            newThreshold = (newCapacity < MAXIMUM_CAPACITY) ? (int) (newCapacity * DEFAULT_LOAD_FACTOR) : Integer.MAX_VALUE;
        }

        threshold = newThreshold;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];

        if (table != null) {
            for (int j = 0; j < oldCapacity; ++j) {
                Node<K, V> element;
                while ((element = table[j]) != null) {
                    table[j] = element.next;
                    int hash = element.hash;
                    int indexInNewTable = hash & (newTable.length - 1);

                    element.next = newTable[indexInNewTable];
                    newTable[indexInNewTable] = element;
                }
            }
        }

        table = newTable;
        return newTable;
    }


    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(K key) {
        int hash = hash(key);
        return hash & (table.length - 1);
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
