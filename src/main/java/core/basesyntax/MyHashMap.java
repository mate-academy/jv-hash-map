package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int ARRAY_SIZE_MULTIPLIER = 2;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(key, value, null, getHashCode(key));
        int indexOfBucket = newNode.hashCode % table.length;
        Node<K, V> workingNode = table[indexOfBucket];
        while (workingNode != null) {
            if (Objects.equals(key, workingNode.key)) {
                workingNode.value = newNode.value;
                return;
            }
            if (workingNode.next == null) {
                workingNode.next = newNode;
                size++;
                return;
            }
            workingNode = workingNode.next;
        }
        table[indexOfBucket] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int indexOfKey = getHashCode(key) % table.length;
        Node<K, V> currentNode = table[indexOfKey];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
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

    private void resize() {
        if (size < threshold) {
            return;
        }
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * ARRAY_SIZE_MULTIPLIER];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    private int getHashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private class Node<K, V> {
        private int hashCode;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next, int bucketIndex) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hashCode = bucketIndex;
        }

        public final String toString() {
            return key + "=" + value;
        }
    }
}
