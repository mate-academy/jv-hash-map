package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K,V> implements MyMap<K,V> {
    private static final int DEFAULT_SIZE = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int bucketsSize = DEFAULT_SIZE;
    private Node<K,V>[] buckets = new Node[bucketsSize];
    private int size = 0;

    @Override
    public void put(K key, V value) {
        int bucketIndex = getHashCode(key) % bucketsSize;
        Node<K,V> currentNode = getNode(key);
        if (currentNode != null) {
            currentNode.value = value;
        } else {
            buckets[bucketIndex] = new Node<>(key, value, buckets[bucketIndex]);
            size++;
        }
        if (size > bucketsSize * DEFAULT_LOAD_FACTOR) {
            grow();
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public V getValue(K key) {
        Node<K,V> currentNode = getNode(key);
        if (currentNode != null) {
            return currentNode.value;
        }
        return null;
    }

    private void grow() {
        int newSize = bucketsSize * 2;
        Node<K,V>[] newNodeArray = new Node[newSize];
        for (int i = 0; i < bucketsSize; i++) {
            Node<K,V> currentNode = buckets[i];
            while (currentNode != null) {
                buckets[i] = buckets[i].next;
                currentNode.next = newNodeArray[getHashCode(currentNode.key) % newSize];
                newNodeArray[getHashCode(currentNode.key) % newSize] = currentNode;
                currentNode = buckets[i];
            }
        }
        bucketsSize = newNodeArray.length;
        buckets = newNodeArray;
    }

    private Node<K,V> getNode(K key) {
        int hash = getHashCode(key);
        Node<K,V> testNode = buckets[hash % bucketsSize];
        while (testNode != null) {
            if (Objects.equals(key, testNode.key)) {
                return testNode;
            }
            testNode = testNode.next;
        }
        return testNode;
    }

    private int getHashCode(K key) {
        return (key == null) ? 0 : (17 * 31 + key.hashCode() >>> 1);
    }

    private class Node<K,V> {
        private V value;
        private final K key;
        private Node<K, V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.value = value;
            this.key = key;
            this.next = next;
        }
    }
}
