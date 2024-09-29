package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int CAPACITY_MULTIPLIER = 2;

    private Node<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int position = getBucketIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);

        if (table[position] == null) {
            table[position] = newNode;

            size++;
        } else {
            Node<K, V> currElem = getElementByKeyOrLast(key, position);

            if (key == currElem.getKey()
                            || currElem.getKey() != null && currElem.getKey().equals(key)
            ) {
                currElem.setValue(value);
            } else {
                currElem.setNext(newNode);

                size++;
            }
        }

        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int position = getBucketIndex(key);

        if (table[position] == null) {
            return null;
        }

        Node<K, V> currElem = getElementByKeyOrLast(key, position);

        return currElem.getValue();
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : getKeyHashCode(key) % table.length;
    }

    private Node<K, V> getElementByKeyOrLast(K key, int position) {
        Node<K, V> currElem = table[position];

        while (currElem.getNext() != null) {
            if (Objects.equals(currElem.getKey(), key)) {
                return currElem;
            }

            currElem = currElem.getNext();
        }

        return currElem;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] prevTable = table;

        table = new Node[table.length * CAPACITY_MULTIPLIER];
        size = 0;

        for (Node<K, V> node : prevTable) {
            if (node != null) {
                Node<K, V> currElem = node;

                while (currElem != null) {
                    put(currElem.getKey(), currElem.getValue());

                    currElem = currElem.getNext();
                }
            }
        }
    }

    private int getKeyHashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
}
