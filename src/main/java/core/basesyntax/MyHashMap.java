package core.basesyntax;

import java.util.Map;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final int defaultCapacity = 16;
    private final double defaultLoadFactor = 0.75;
    private int size = 0;
    private int capacity;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.capacity = defaultCapacity;
        this.table = new Node[capacity];
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            Node<K, V> newNode = new Node<>(hash(key), key, value, table[index]);
            table[index] = newNode;
            size++;
            return;
        }

        while (currentNode != null) {
            if ((currentNode.key == null && key == null)
                    || (currentNode.key != null && currentNode.key.equals(key))) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                Node<K, V> newNode = new Node<>(hash(key), key, value, null);
                currentNode.next = newNode;
                size++;
                break;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node currentNode = table[index];
        if (currentNode != null) {
            do {
                if (key == null || currentNode.key == null) {
                    if (currentNode.key == key) {
                        return (V) currentNode.value;
                    }
                } else {
                    if (currentNode.key.equals(key)) {
                        return (V) currentNode.value;
                    }
                }
                currentNode = currentNode.next;
            } while (currentNode != null);
        }
        return null;
    }

    public int getIndex(K key) {
        return Math.abs(hash(key)) % capacity;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode() & 0x7fffffff;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void checkSize() {
        if ((double) size / capacity >= defaultLoadFactor) {
            resizeHashMap();
        }
    }

    public void resizeHashMap() {
        capacity *= 2;
        Node<K, V>[] newTable = new Node[capacity];

        for (Node<K, V> current : table) {
            while (current != null) {
                int index = getIndex((K) current.key);
                Node<K, V> newNode =
                        new Node<>(hash((K) current.key), current.key, current.value, null);

                if (newTable[index] == null) {
                    newTable[index] = newNode;
                } else {
                    Node<K, V> currentNodeNewTable = newTable[index];
                    while (currentNodeNewTable.next != null) {
                        currentNodeNewTable = currentNodeNewTable.next;
                    }
                    currentNodeNewTable.next = newNode;
                }

                current = current.next;
            }
        }

        table = newTable;
    }
}
