package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = findIndex(hash(key));
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> iterator = table[index];
            while (iterator.next != null || Objects.equals(iterator.key, key)) {
                if (iterator.key == key || Objects.equals(iterator.key, key)) {
                    iterator.value = newNode.value;
                    return;
                }
                iterator = iterator.next;
            }
            iterator.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = findIndex(hash(key));
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
        int capacity = table.length << 1;
        threshold = (int) (capacity * LOAD_FACTOR);
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
        return Math.abs(hashValue & table.length - 1);
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private class Node<K, V> {
        private final K key;
        private Node<K, V> next;
        private V value;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
