package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75f;

    private int size;
    private Node<K, V>[] bucketsArray;

    public MyHashMap() {
        bucketsArray = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (putVal(key, value, bucketsArray)) {
            resizeIfNeeded();
        }
    }

    @Override
    public V getValue(K key) {
        int pos = getKeyIndex(key, bucketsArray);
        Node<K, V> currentNode = bucketsArray[pos];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
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

    private void resizeIfNeeded() {
        if (++size > bucketsArray.length * LOAD_FACTOR) {
            Node<K, V>[] newArray = (Node<K, V>[]) new Node[bucketsArray.length * GROW_FACTOR];
            for (Node<K, V> node : bucketsArray) {
                if (node != null) {
                    Node<K, V> tempNode = node;
                    while (tempNode != null) {
                        putVal(tempNode.key, tempNode.value, newArray);
                        tempNode = tempNode.next;
                    }
                }
            }
            bucketsArray = newArray.clone();
        }
    }

    private int getKeyIndex(K key, Node<K, V>[] array) {
        int pos = key == null ? 0 : key.hashCode() % array.length;
        return Math.abs(pos);
    }

    private boolean putVal(K key, V value, Node<K, V>[] nodeArray) {
        int pos = getKeyIndex(key, nodeArray);
        Node<K, V> currentNode = nodeArray[pos];

        while (currentNode != null && currentNode.next != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return false;
            }
            currentNode = currentNode.next;
        }

        if (currentNode == null) {
            currentNode = new Node<>(key, value);
            nodeArray[pos] = currentNode;
        } else {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return false;
            }
            currentNode.next = new Node<>(key, value);
        }
        return true;
    }

    private static class Node<K, V> {
        private final K key;
        private final int hash;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            hash = key == null ? 0 : key.hashCode();
        }
    }
}
