package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOADING_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;
    private int threshold;
    private int size;
    private Node<K, V>[] bucketsArray;

    public MyHashMap() {
        threshold = (int) (DEFAULT_CAPACITY * LOADING_FACTOR);
        bucketsArray = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newElement = new Node<>(key, value);

        if (size >= threshold) {
            resize();
        }
        addElement(newElement);
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByHashCode(key);
        Node<K, V> currentNode = bucketsArray[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.nextItemInBucket;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexByHashCode(Object key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % bucketsArray.length);
    }

    private void resize() {
        final Node<K, V>[] oldBucketsArray = bucketsArray;
        bucketsArray = new Node[bucketsArray.length * RESIZE_MULTIPLIER];
        threshold = (int) (bucketsArray.length * LOADING_FACTOR);
        size = 0;
        for (Node<K, V> node : oldBucketsArray) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                addElement(new Node<K, V>(currentNode.key, currentNode.value));
                currentNode = currentNode.nextItemInBucket;
            }
        }
    }

    private void addElement(Node<K, V> node) {
        int index = getIndexByHashCode(node.key);
        if (bucketsArray[index] == null) {
            bucketsArray[index] = node;
            size++;
            return;
        }
        if (Objects.equals(bucketsArray[index].key, node.key)) {
            bucketsArray[index].value = (node.value);
        } else {
            Node<K, V> currentNode = getLastOrEqualNode(bucketsArray[index], node.key);
            node.nextItemInBucket = currentNode.nextItemInBucket;
            currentNode.nextItemInBucket = node;
        }
    }

    private Node<K, V> getLastOrEqualNode(Node<K,V> currentNode, K key) {
        while (currentNode.nextItemInBucket != null) {
            if (Objects.equals(currentNode.nextItemInBucket.key, key)) {
                return currentNode;
            }
            currentNode = currentNode.nextItemInBucket;
        }
        size++;
        return currentNode;
    }

    private boolean keyIsNew(K key) {
        if (bucketsArray[getIndexByHashCode(key)] == null) {
            return true;
        }
        Node<K, V> current = bucketsArray[getIndexByHashCode(key)];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                return false;
            }
            current = current.nextItemInBucket;
        }
        return true;

    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextItemInBucket;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
