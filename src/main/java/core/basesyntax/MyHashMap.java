package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] bucketsList;
    private int threshold;

    public MyHashMap() {
        bucketsList = new Node[DEFAULT_CAPACITY];
        threshold = (int) DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = hash(key);
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> currentNode = bucketsList[index];
        Node<K, V> prevNode = null;
        if (bucketsList[index] == null) {
            bucketsList[index] = newNode;
        } else {
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, newNode.key)) {
                    currentNode.value = newNode.value;
                    return;
                }
                prevNode = currentNode;
                currentNode = currentNode.next;
            }
            prevNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> currentNode = bucketsList[index];
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
        size = 0;
        Node<K, V>[] oldBucketList = bucketsList;
        bucketsList = new Node[oldBucketList.length * 2];
        for (Node<K, V> bucket : oldBucketList) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % bucketsList.length);
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
