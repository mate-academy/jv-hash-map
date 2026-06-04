package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K,V> [] table;
    private int size;
    private int capacity;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K,V> newNode = new Node<>(key, value);
        int index = mapIndex(key, capacity);
        Node<K,V> currNode = table[index];

        if (currNode == null) {
            table[index] = newNode;
            size++;
        }

        while (currNode != null) {
            if(Objects.equals(currNode.key, key)) {
                currNode.value = value;
                return;
            } else if(currNode.next == null) {
                currNode.next = newNode;
                size++;
                return;
            }

            currNode = currNode.next;
        }
    }

    private void resize() {
        capacity = capacity << 1;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);

        Node<K,V>[] newTable = new Node[capacity];

        for (Node<K, V> kvNode : table) {
            Node<K, V> currNode = kvNode;

            while (currNode != null) {
                Node<K, V> nextNode = currNode.next;
                int index = mapIndex(currNode.key, capacity);
                currNode.next = newTable[index];
                newTable[index] = currNode;

                currNode = nextNode;
            }
        }

        table = newTable;
    }

    private int mapIndex(K key, int capacity) {
        return key != null ? key.hashCode() & (capacity - 1) : 0;
    }


    @Override
    public V getValue(K key) {
        if (size > 0) {
            int index = mapIndex(key, capacity);
            Node<K, V> currNode = table[index];
            while (currNode != null) {
                if (Objects.equals(currNode.key, key)) {
                    return currNode.value;
                }
                currNode = currNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
