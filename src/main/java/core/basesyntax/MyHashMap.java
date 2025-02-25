package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int CONSTANT_FOR_HASH = 0x7FFFFFFF;
    private static final double LOAD_FACTOR = 0.75;
    private Bucket<K, V>[] myHashMap;
    private int capacity;
    private int threshold;
    private int size;

    private Node<K, V> currentNode = new Node<>();
    private int indexBucket;

    public MyHashMap() {
        createMyHashMap();
    }

    @Override
    public void put(K key, V value) {
        checkThreshold();
        insertNodeInMap(myHashMap, new Node<>(key, value));
    }

    @Override
    public V getValue(K key) {
        indexBucket = indexByKey(key);
        if (myHashMap[indexBucket] != null) {
            currentNode = myHashMap[indexBucket].node;
            for (int i = 0; i < myHashMap[indexBucket].bucketSize; i++) {
                if (Objects.equals(currentNode.key, key)) {
                    return currentNode.value;
                }
                if (currentNode.next == null) {
                    break;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkThreshold() {
        if (size >= threshold) {
            capacity = capacity << 1;
            resize();
        }
    }

    private Bucket<K, V>[] createMyHashMap() {
        if (capacity == 0) {
            capacity = DEFAULT_CAPACITY;
        }
        threshold = (int) (capacity * LOAD_FACTOR);
        size = 0;
        return myHashMap = new Bucket[capacity];
    }

    private void resize() {
        Bucket<K, V>[] copyMap = myHashMap;
        myHashMap = createMyHashMap();
        for (Bucket<K, V> bucket : copyMap) {
            if (bucket == null) {
                continue;
            }
            currentNode = bucket.node;
            for (int i = 0; i < bucket.bucketSize; i++) {
                Node<K, V> nextNodeInBucket = currentNode.next;
                insertNodeInMap(myHashMap, currentNode);
                if (nextNodeInBucket == null) {
                    break;
                }
                currentNode = nextNodeInBucket;
            }
        }
    }

    private void insertNodeInMap(Bucket<K, V>[] map, Node<K, V> node) {
        indexBucket = indexByKey(node.key);
        if (map[indexBucket] != null) {
            currentNode = map[indexBucket].node;
            for (int i = 0; i < map[indexBucket].bucketSize; i++) {
                if (Objects.equals(currentNode.key, node.key)) {
                    currentNode.value = node.value;
                    return;
                }
                if (currentNode.next == null) {
                    break;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = node;
            node.next = null;
        }
        if (map[indexBucket] == null) {
            map[indexBucket] = new Bucket<>(node);
            node.next = null;
        }
        map[indexBucket].bucketSize++;
        size++;
    }

    private int indexByKey(K key) {
        return currentNode.calculateHash(key) % capacity;
    }

    private static class Bucket<K, V> {
        private Node<K, V> node;
        private int bucketSize;

        private Bucket(K key, V value) {
            this.node = new Node<>(key, value);
        }

        private Bucket(Node<K, V> node) {
            this.node = node;
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            hash = calculateHash(key);
        }

        private Node() {
        }

        private int calculateHash(K key) {
            return key == null ? 0 : key.hashCode() & CONSTANT_FOR_HASH;
        }
    }
}
