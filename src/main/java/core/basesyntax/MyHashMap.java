package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int RESIZE_FACTOR = 2;
    private static final float SIZE_FACTOR = 0.75f;
    private int size;
    private int threshHold;
    private int capacity;
    private Node<K, V>[] hashTable;

    public MyHashMap() {
        hashTable = new Node[DEFAULT_CAPACITY];
        threshHold = (int) (DEFAULT_CAPACITY * SIZE_FACTOR);
        capacity = DEFAULT_CAPACITY;

    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> node = new Node<>(key, value);
        int index = getIndex(key);
        if (hashTable[index] == null) {
            hashTable[index] = node;
            size++;
        } else {
            Node<K, V> newNode = hashTable[index];
            while (newNode != null) {
                if (Objects.equals(node.key, newNode.key)) {
                    newNode.value = value;
                    return;
                }
                if (newNode.next == null) {
                    newNode.next = node;
                    size++;
                    return;
                }
                newNode = newNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> getNode = hashTable[getIndex(key)];
        while (getNode != null) {
            if (Objects.equals(getNode.key, key)) {
                return getNode.value;
            }
            getNode = getNode.next;
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
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }

    private int hash(K key) {
        return Objects.hash(key) % hashTable.length;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(hash(key));
    }

    private void resize() {
        if (size == threshHold) {
            capacity = capacity * RESIZE_FACTOR;
            threshHold = capacity * DEFAULT_CAPACITY;
            size = 0;
            Node<K, V>[] oldTable = hashTable;
            hashTable = new Node[capacity];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}

