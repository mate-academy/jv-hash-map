package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node[] buckets;

    public MyHashMap() {
        this.buckets = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == (buckets.length * DEFAULT_LOAD_FACTOR)) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value);
        int bucketIndex = bucketIndex(key);
        Node currentNode = buckets[bucketIndex];
        if (currentNode == null) {
            buckets[bucketIndex] = newNode;
            size++;
        }
        while (currentNode != null) {
            if (Objects.equals(currentNode.getKey(), key)) {
                currentNode.setValue(value);
                break;
            }
            if (currentNode.getNext() == null) {
                currentNode.setNext(newNode);
                size++;
                break;
            }
            currentNode = currentNode.getNext();
        }
    }

    private void resize() {
        Node[] bufArray = buckets;
        buckets = new Node[buckets.length * 2];
        size = 0;
        for (Node<K, V> node : bufArray) {
            Node<K, V> newNode = node;
            while (newNode != null) {
                put(newNode.getKey(), newNode.getValue());
                newNode = newNode.getNext();
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = buckets[bucketIndex(key)];
        while (node != null) {
            if (node.getKey() == (key)) {
                return node.getValue();
            } else if (node.getKey() != null && node.getKey().equals(key)) {
                return node.getValue();
            } else {
                node = node.getNext();
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int bucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % buckets.length);
    }
}
