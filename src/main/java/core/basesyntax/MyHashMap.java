package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int index = getIndexOfBucket(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> prevNode = null;
            Node<K, V> curNode = table[index];
            while (curNode != null) {
                if (Objects.equals(key, curNode.key)) {
                    curNode.value = value;
                    return;
                }
                prevNode = curNode;
                curNode = curNode.next;
            }
            prevNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexOfBucket(key);
        Node<K, V> findNode = table[index];
        while (findNode != null) {
            if (Objects.equals(findNode.key, key)) {
                return findNode.value;
            }
            findNode = findNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexOfBucket(K key) {
        if (key == null) {
            return 0;
        }
        return getHash(key) % table.length;
    }

    private int getIndexOfBucket(K key, int divider) {
        return key == null ? 0 : Math.abs(key.hashCode() % divider);
    }

    private int getHash(K key) {
        return Math.abs(key == null ? 0 : key.hashCode());
    }

    private void resizeIfNeeded() {
        if (size == threshold) {
            int newCapacity = table.length * 2;
            Node<K, V>[] newTable = new Node[newCapacity];
            for (int i = 0; i < table.length; i++) {
                while (table[i] != null) {
                    int index = getIndexOfBucket(table[i].key, newCapacity);
                    Node<K, V> next = table[i].next;
                    table[i].next = newTable[index];
                    newTable[index] = table[i];
                    table[i] = next;
                }
            }
            threshold = (int) (newTable.length * LOAD_FACTOR);
            table = newTable;
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
