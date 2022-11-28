package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int threshold;
    private int capacity;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0) {
            table = new Node[DEFAULT_CAPACITY];
        }
        capacity = table.length;
        threshold = (int) (capacity * LOAD_FACTOR);
        if (size >= threshold) {
            table = resize();
        }
        int index = Math.abs(hash(key) % capacity);
        if (table[index] != null) {
            Node<K, V> nextNode = table[index];
            while (nextNode.next != null) {
                if (Objects.equals(nextNode.key, key)) {
                    nextNode.value = value;
                    return;
                }
                nextNode = nextNode.next;
            }
            if (Objects.equals(nextNode.key, key)) {
                nextNode.value = value;
                return;
            }
            nextNode.next = putNodeInNullPosition(key, value);
        }
        if (table[index] == null) {
            table[index] = putNodeInNullPosition(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        V value = null;
        if (table != null || table.length != 0) {
            int index = Math.abs(hash(key) % table.length);
            if (table[index] != null) {
                Node<K, V> nextPair = table[index];
                while (nextPair != null) {
                    if (Objects.equals(nextPair.key, key)) {
                        value = nextPair.value;
                    }
                    nextPair = nextPair.next;
                }
            }
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private Node<K, V>[] resize() {
        capacity = table.length;
        capacity = capacity * 2;
        size = 0;
        Node<K, V>[] resizeTable = new Node[capacity];
        for (int i = 0; i < table.length; i++) {
            while (table[i] != null) {
                int index = Math.abs(table[i].hash % capacity);
                if (resizeTable[index] != null) {
                    Node<K, V> nextNode = resizeTable[index];
                    while (nextNode.next != null) {
                        nextNode = nextNode.next;
                    }
                    nextNode.next = putNodeInNullPosition(table[i].key, table[i].value);
                }
                if (resizeTable[index] == null) {
                    resizeTable[index] = putNodeInNullPosition(table[i].key, table[i].value);
                }
                table[i] = table[i].next;
            }
        }
        return resizeTable;
    }

    private Node<K, V> putNodeInNullPosition(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        size++;
        return node;
    }

    class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.hash = (key == null) ? 0 : key.hashCode();
            this.key = key;
            this.value = value;
        }
    }
}
