package core.basesyntax;

import java.util.Arrays;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int capacity;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    private int findIndex(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()) % table.length);
    }

    private Node<K, V>[] resize() {
        capacity = table.length << 1;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        return table;
    }

    private void checkTableSize(int size) {
        if (size >= (int) (capacity * LOAD_FACTOR)) {
            resize();
        }
    }

    @Override
    public void put(K key, V value) {
        checkTableSize(size);
        Node<K, V> node = new Node<>(key, value, null);
        Node<K, V> tempNode = table[findIndex(key)];
        if (tempNode == null) {
            table[findIndex(key)] = node;
            size++;
        } else {
            while (tempNode != null) {
                if (key == null && tempNode.getKey() == null) {
                    tempNode.setValue(value);
                    break;
                } else if (key == tempNode.getKey()
                        || key != null && key.equals(tempNode.getKey())) {
                    tempNode.setValue(value);
                    break;
                } else if (tempNode.next == null) {
                    tempNode.next = node;
                    size++;
                    break;
                }
                tempNode = tempNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> tempNode = table[findIndex(key)];
        while (tempNode != null) {
            if (key == tempNode.getKey() || key != null && key.equals(tempNode.getKey())) {
                return tempNode.getValue();
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node next) {
            this.hash = hash;
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
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyHashMap<?, ?> myHashMap = (MyHashMap<?, ?>) o;
        return capacity == myHashMap.capacity && size == myHashMap.size
                && Arrays.equals(table, myHashMap.table);
    }
}
