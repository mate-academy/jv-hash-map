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
        if (table[getIndexFromKey(key)] != null) {
            Node<K, V> localCurrent = table[getIndexFromKey(key)];
            while (localCurrent != null) {
                if (Objects.equals(localCurrent.key, key)) {
                    localCurrent.value = value;
                    return;
                } else if (localCurrent.next == null) {
                    break;
                }
                localCurrent = localCurrent.next;
            }
            localCurrent.next = new Node<K, V>(key, value, null);
            size++;
            return;
        }
        table[getIndexFromKey(key)] = new Node<K, V>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[getIndexFromKey(key)];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexFromKey(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
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
