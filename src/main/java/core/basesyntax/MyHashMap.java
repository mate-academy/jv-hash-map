package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private int size;

    private Node<K, V>[] bucketArray;

    public MyHashMap() {
        bucketArray = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        if (bucketArray[bucketIndex] == null) {
            bucketArray[bucketIndex] = new Node<>(key, value);
        } else {
            Node<K, V> current = bucketArray[bucketIndex];
            while (current != null) {
                if (Objects.equals(key, current.key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            }
            current.next = new Node<>(key, value);
        }

        size++;
        sizeCheck();
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> current = bucketArray[bucketIndex];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
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

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % bucketArray.length);
    }

    private void sizeCheck() {
        if ((double) size / bucketArray.length > LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldBucketArray = bucketArray;
        bucketArray = new Node[oldBucketArray.length * 2];
        size = 0;
        for (Node<K, V> kvNode : oldBucketArray) {
            while (kvNode != null) {
                this.put(kvNode.key, kvNode.value);
                kvNode = kvNode.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
