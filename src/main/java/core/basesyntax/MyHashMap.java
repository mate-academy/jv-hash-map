package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int capacity;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = INITIAL_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
        this.table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        int hashValue = hash(key);
        int index = findIndex(hashValue);
        if (table[index] == null) {
            table[index] = new Node<>(hashValue, key, value, null);
        } else {
            Node<K, V> iterator = table[index];
            while (true) {
                if (iterator.key == key || Objects.equals(iterator.key, key)) {
                    iterator.value = value;
                    return;
                }
                if (iterator.next == null) {
                    break;
                }
                iterator = iterator.next;
            }
            iterator.next = new Node<>(hashValue, key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = findIndex(hash(key));
        if (table[index] == null) {
            return null;
        }
        Node<K, V> iterator = table[index];
        while (iterator != null) {
            if (Objects.equals(iterator.key, key)) {
                return iterator.value;
            }
            iterator = iterator.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkSize() {
        if (size == threshold) {
            resize();
        }
    }

    private void resize() {
        capacity = capacity << 1;
        threshold = threshold << 1;
        Node<K, V>[] temporaryTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
        transfer(temporaryTable);
    }

    private void transfer(Node<K, V>[] temporaryTable) {
        for (Node<K, V> element : temporaryTable) {
            while (element != null) {
                put(element.key, element.value);
                element = element.next;
            }
        }
    }

    private int findIndex(int hashValue) {
        return Math.abs(hashValue & capacity - 1);
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private Node<K, V> next;
        private V value;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
