package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private Node<K, V>[] table;
    private final double loadFactor;
    private int size;
    private int capacity;
    private final int defaultInitialCapacity = 1 << 4;
    private final double defaultLoadFactor = 0.75;

    public MyHashMap() {
        this.loadFactor = defaultLoadFactor;
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        if (table == null) {
            resize();
        }
        int pos = hash % capacity;
        if (table[pos] == null) {
            table[pos] = new Node<>(key, value, hash, null);
            sizePlus();
        } else if (table[pos].hash == hash && (Objects.equals(key, table[pos].key))) {
            table[pos].value = value;
        } else {
            Node<K, V> tempX;
            Node<K, V> tempY = table[pos];
            while (true) {
                if ((tempX = tempY.next) == null) {
                    tempY.next = new Node<>(key, value, hash, null);
                    sizePlus();
                    break;
                }
                if (tempX.hash == hash && (Objects.equals(key, tempX.key))) {
                    tempX.value = value;
                    break;
                }
                tempY = tempX;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int hash = hash(key);
        int pos = hash % capacity;
        for (Node<K, V> x = table[pos]; x != null; x = x.next) {
            if (x.hash == hash && Objects.equals(key, x.key)) {
                return x.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private final int hash;
        private Node<K, V> next;

        private Node(K key, V value, int hash, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    private int hash(Object key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()));
    }

    private void resize() {
        if (table == null) {
            capacity = defaultInitialCapacity;
            table = (Node<K, V>[]) new Node[capacity];
            return;
        }
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity = capacity << 1];
        putAll(oldTable);
    }

    private void putAll(Node<K, V>[] oldTable) {
        size = 0;
        for (Node<K, V> bucket : oldTable) {
            for (Node<K, V> x = bucket; x != null; x = x.next) {
                put(x.key, x.value);
            }
        }
    }

    private void sizePlus() {
        if (++size > loadFactor * capacity) {
            resize();
        }
    }
}

