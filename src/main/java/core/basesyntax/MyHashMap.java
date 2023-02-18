package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_CAPACITY = 16;
    public static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) Math.round(DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        if (key == null || key.hashCode() % table.length == 0) {
            zeroIndexPutHandler(key, value);
        } else if (table[getIndexFromKey(key)] != null) {
            nonEmptyBucketPutHandler(key, value);
        } else {
            table[getIndexFromKey(key)] = new Node<K, V>(key, value, null);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null && table[0] != null) {
            return zeroIndexReadHandler();
        }
        if (table[getIndexFromKey(key)] != null) {
            return nonEmptyBucketReadHandler(key);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private V nonEmptyBucketReadHandler(K key) {
        Node<K, V> current = table[getIndexFromKey(key)];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    private V zeroIndexReadHandler() {
        Node<K, V> current = table[0];
        while (current != null) {
            if (current.key == null) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    private void zeroIndexPutHandler(K key, V value) {
        Node<K, V> localCurrent = table[0];
        if (localCurrent == null) {
            table[0] = new Node<K, V>(key, value, null);
            size++;
            return;
        }
        if (localCurrent.key == key) {
            localCurrent.value = value;
            return;
        }
        while (localCurrent.next != null) {
            if (localCurrent.key == key) {
                localCurrent.value = value;
                return;
            }
            localCurrent = localCurrent.next;
        }
        localCurrent.next = new Node<K, V>(key, value, null);
        size++;
    }

    private void nonEmptyBucketPutHandler(K key, V value) {
        Node<K, V> current = table[getIndexFromKey(key)];
        if (Objects.equals(current.key, key)) {
            current.value = value;
            return;
        }
        while (current.next != null) {
            current = current.next;
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
        }
        current.next = new Node<K, V>(key, value, null);
        size++;
    }

    private int getIndexFromKey(K key) {
        int currentIndex = key.hashCode() % table.length;
        return currentIndex < 0 ? -1 * currentIndex : currentIndex;
    }

    private void resize() {
        size = 0;
        threshold = threshold << 1;
        Node<K, V>[] temp = table;
        table = (Node<K, V>[]) new Node[table.length << 1];
        for (Node<K, V> kvNode : temp) {
            if (kvNode != null) {
                put(kvNode.key, kvNode.value);
                while (kvNode.next != null) {
                    kvNode = kvNode.next;
                    put(kvNode.key, kvNode.value);
                }
            }
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
