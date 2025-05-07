package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INCREASE_COEFFICIENT = 2;
    private int size;
    private float threshold;
    private Node<K, V>[] nodeArray;

    public MyHashMap() {
        nodeArray = new Node[DEFAULT_CAPACITY];
        threshold = DEFAULT_CAPACITY * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> currentNode = new Node<>(key, value, null);
        resizeArray();
        Node<K, V> newNode = nodeArray[calculateIndex(key)];
        while (newNode != null) {
            if (Objects.equals(key, newNode.key)) {
                newNode.value = value;
                return;
            }
            if (newNode.next == null) {
                newNode.next = currentNode;
                size++;
                return;
            }
            newNode = newNode.next;
        }
        nodeArray[calculateIndex(key)] = currentNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> thisNode = nodeArray[calculateIndex(key)];
        while (thisNode != null) {
            if (Objects.equals(key, thisNode.key)) {
                return thisNode.value;
            }
            thisNode = thisNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private final K key;
        private V value;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resizeArray() {
        if (size == threshold) {
            threshold *= INCREASE_COEFFICIENT;
            Node<K, V>[] prevNodeArray = nodeArray;
            nodeArray = new Node[prevNodeArray.length * INCREASE_COEFFICIENT];
            size = 0;
            for (Node<K, V> node : prevNodeArray) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getIndex(int keyHash) {
        return keyHash % nodeArray.length;
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int calculateIndex(K key) {
        return getIndex(getHash(key));
    }
}
