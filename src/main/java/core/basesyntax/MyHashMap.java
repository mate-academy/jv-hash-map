package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOADING_FACTOR = 0.75f;
    private int threshold;
    private int capacity;
    private int size;
    private Node<K, V>[] bucketsArray;

    public MyHashMap() {
        threshold = (int) (DEFAULT_CAPACITY * LOADING_FACTOR);
        bucketsArray = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newElement = new Node<>(key, value);
        if (keyIsNew(key)) {
            size++;
        }
        if (size == threshold) {
            resize();
        }
        addElement(newElement);
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByHashCode(key);
        if (bucketsArray[index] != null) {
            if (Objects.equals(bucketsArray[index].getKey(), key)) {
                return bucketsArray[index].getValue();
            }
            Node<K, V> currentNode = bucketsArray[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.getKey(), key)) {
                    return currentNode.getValue();
                }
                currentNode = currentNode.nextItemInBucket;
            }
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
        int index = (key.hashCode() % capacity);
        return index >= 0 ? index : index * -1;
    }

    private void resize() {
        capacity *= 2;
        threshold = (int) (capacity * LOADING_FACTOR);
        Node<K, V>[] oldBucketsArray = bucketsArray;
        bucketsArray = new Node[capacity];
        for (int i = 0; i < oldBucketsArray.length; i++) {
            if (oldBucketsArray[i] != null) {
                Node<K, V> currentNode = oldBucketsArray[i];
                while (currentNode != null) {
                    addElement(new Node<K, V>(currentNode.key, currentNode.value));
                    currentNode = currentNode.nextItemInBucket;

                }
            }
        }
    }

    private void addElement(Node<K, V> node) {
        int index = node.getKey() == null ? 0 : getIndexByHashCode(node.getKey());
        if (bucketsArray[index] == null) {
            bucketsArray[index] = node;
            return;
        }
        if (Objects.equals(bucketsArray[index].getKey(), node.getKey())) {
            bucketsArray[index].setValue(node.getValue());
        } else {
            Node<K, V> currentNode = getLastOrEqualNode(bucketsArray[index], node.getKey());
            node.nextItemInBucket = currentNode.nextItemInBucket;
            currentNode.nextItemInBucket = node;
        }
    }

    private Node<K, V> getLastOrEqualNode(Node<K,V> currentNode, K key) {
        while (currentNode.nextItemInBucket != null) {
            if (Objects.equals(currentNode.nextItemInBucket.getKey(), key)) {
                return currentNode;
            }
            currentNode = currentNode.nextItemInBucket;
        }
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
            this.nextItemInBucket = null;
        }

        public K getKey() {
            return key;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public V getValue() {
            return value;
        }
    }
}
