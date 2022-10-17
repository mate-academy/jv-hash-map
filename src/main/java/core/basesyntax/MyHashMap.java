package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final int MAX_CAPACITY = 1 << 30;
    private static final int SIZE_MULTIPLIER = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int currentCapacity;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[INITIAL_CAPACITY];
        this.currentCapacity = INITIAL_CAPACITY;
        this.threshold = (int) (INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = defineCell(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
            table[index].value = value;
            size++;
        } else {
            Node<K, V> currNode = table[index];
            while (currNode != null) {
                if (currNode.key == key || Objects.equals(key, currNode.key)) {
                    currNode.value = value;
                    return;
                } else if (currNode.next == null) {
                    currNode.next = newNode;
                    break;
                }
                currNode = currNode.next;
            }
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[defineCell(key)];
        while (currentNode != null) {
            if (currentNode.key == key || Objects.equals(key, currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
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

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return key.equals(node.key) && value.equals(node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    private int defineCell(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % currentCapacity;
    }

    private void resize() {
        if (size < MAX_CAPACITY && size == threshold) {
            currentCapacity *= SIZE_MULTIPLIER;
            threshold = (int) (DEFAULT_LOAD_FACTOR * currentCapacity);
            transfer(currentCapacity);
        }
    }

    private void transfer(int newCapacity) {
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> bucket : oldTable) {
            while (bucket != null) {
                put(bucket.key,bucket.value);
                bucket = bucket.next;
            }
        }
    }
}
