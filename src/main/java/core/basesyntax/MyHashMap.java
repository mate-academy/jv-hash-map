package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int SIZE_INDEX = 2;
    private Node<K, V>[] listOfPair;
    private int size;
    private int treshold;

    public MyHashMap() {
        listOfPair = new Node[DEFAULT_CAPACITY];
        treshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= treshold) {
            resize();
        }
        int index = getIndex(key, listOfPair.length);
        Node<K, V> node = new Node<>(key, value);
        if (listOfPair[index] == null) {
            listOfPair[index] = node;
            size++;
        } else {
            putIfBucketFull(listOfPair[index], node, key, value);
        }
    }

    private void putIfBucketFull(Node<K, V> actual, Node<K, V> node, K key, V value) {
        while (actual != null) {
            if (Objects.equals(actual.key, key)) {
                actual.value = value;
                return;
            }
            if (actual.next == null) {
                actual.next = node;
                size++;
                return;
            }
            actual = actual.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key, listOfPair.length);
        Node<K, V> current = listOfPair[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newSize = listOfPair.length * SIZE_INDEX;
        Node<K, V>[] newListOfPair = new Node[newSize];
        for (Node<K, V> someNode : listOfPair) {
            while (someNode != null) {
                Node<K, V> next = someNode.next;
                int index = getIndex(someNode.key, newSize);
                someNode.next = newListOfPair[index];
                newListOfPair[index] = someNode;
                someNode = next;
            }
        }
        listOfPair = newListOfPair;
        treshold = (int) (newSize * DEFAULT_LOAD_FACTOR);
    }

    private int getIndex(K key, int length) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % length);
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
