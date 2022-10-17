package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INCREASE_FACTOR = 1;
    private int capacity = 16;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        threshold = (int) (LOAD_FACTOR * capacity);
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNecessary();
        putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (size == 0 || table[index] == null) {
            return null;
        }
        Node<K, V> tempNode = table[index];
        while (tempNode != null) {
            if (isEquals(tempNode, key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeIfNecessary() {
        if (threshold == size) {
            Node<K, V>[] tempArr = table;
            capacity = capacity << INCREASE_FACTOR;
            transformArray(tempArr);
            threshold = (int) (LOAD_FACTOR * capacity);
        }
    }

    private void putValue(K key, V value) {
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        iterateBucketAndPut(table[index], key, value);
    }

    private int getHash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(K key) {
        return Math.abs(getHash(key)) % capacity;
    }

    private void iterateBucketAndPut(Node<K, V> node, K key, V value) {
        Node<K, V> tempNode = node;
        while (tempNode != null) {
            if (isEquals(tempNode, key)) {
                tempNode.value = value;
                return;
            }
            if (tempNode.next == null) {
                tempNode.next = new Node<>(key, value, null);
                size++;
                return;
            }
            tempNode = tempNode.next;
        }
    }

    private boolean isEquals(Node<K, V> temp, K key) {
        return Objects.equals(temp.key, key);
    }

    private void transformArray(Node<K, V>[] tempArr) {
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : tempArr) {
            if (node != null) {
                Node<K, V> tempNode = node;
                while (tempNode != null) {
                    putValue(tempNode.key, tempNode.value);
                    tempNode = tempNode.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
