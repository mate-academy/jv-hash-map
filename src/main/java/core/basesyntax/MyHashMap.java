package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int capacity;
    private double threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = table.length;
        threshold = DEFAULT_CAPACITY * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key, table);
        Node<K, V> element = table[index];
        Node<K, V> newElement = new Node<>(key, value);
        if (element == null) {
            table[index] = newElement;
            size++;
        } else {
            while (element.next != null) {
                if (Objects.equals(element.key, key)) {
                    element.value = value;
                    return;
                }
                element = element.next;
            }
            if (Objects.equals(element.key, key)) {
                element.value = value;
                return;
            }
            element.next = newElement;
            size++;
            if (size >= threshold) {
                capacity = table.length * 2;
                threshold = capacity * LOAD_FACTOR;
                resize(capacity);
            }
        }

    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key, table);
        Node<K, V> element = table[index];
        if (element == null) {
            return null;
        }
        while (element != null) {
            if (Objects.equals(element.key, key)) {
                return element.value;
            }
            element = element.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int getIndex(Object key, Node<K, V>[] table) {
        if (key != null) {
            return Math.abs(key.hashCode()) % table.length;
        }
        return 0;
    }

    public void resize(int newCapasity) {
        Node<K, V>[] newTable = new Node[newCapasity];
        transfer(newTable);
        table = newTable;
    }

    public void transfer(Node<K, V>[] newTable) {
        for (Node<K, V> tab : table) {
            while (tab != null) {
                Node<K, V> next = tab.next;
                int index = getIndex(tab.key, newTable);
                tab.next = newTable[index];
                newTable[index] = tab;
                tab = next;
            }
        }
    }
}
