package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY_LENGTH = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Bucket<K, V>[] myHashMap;
    private int capacity;
    private int threshold;
    private int size;

    public MyHashMap() {
        createMyHashMap();
    }

    @Override
    public void put(K key, V value) {
        checkThreshold();
        int indexBucket = key == null ? 0 : Math.abs(key.hashCode()) % capacity;
        if (myHashMap[indexBucket] != null) {
            Node<K, V> tempNode = myHashMap[indexBucket].node;
            do {
                if (Objects.equals(tempNode.key, key)) {
                    tempNode.value = value;
                    return;
                }
                if (tempNode.next == null) {
                    break;
                }
                tempNode = tempNode.next;
            }
            while (tempNode != null);

            tempNode.next = new Node<>(key, value);
           // tempNode.next.hash = tempNode.next == null ? 0 : Math.abs(tempNode.next.key.hashCode()) % capacity;
        }
        if (myHashMap[indexBucket] == null) {
            myHashMap[indexBucket] = new Bucket<K, V>(key, value);
           // myHashMap[indexBucket].node.hash = 0;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int indexBucket = key == null ? 0 : Math.abs(key.hashCode()) % capacity;
        if (myHashMap[indexBucket] != null) {
            Node<K, V> currentNode = myHashMap[indexBucket].node;
            do {
                if (Objects.equals(currentNode.key, key)) {
                    return currentNode.value;
                }
                if (currentNode.next == null) {
                    break;
                }
                currentNode = currentNode.next;
            }
            while (currentNode != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkThreshold() {
        if (size > threshold) {
            capacity = capacity << 1;
            resize();
        }
    }

    private Bucket<K, V>[] createMyHashMap() {
        if (capacity == 0) {
            capacity = DEFAULT_CAPACITY_LENGTH;
        }
        threshold = (int) (capacity * LOAD_FACTOR);
        size = 0;
        return myHashMap = new Bucket[capacity];
    }

    private void resize() {
        Bucket<K, V>[] copyMyHashMap = myHashMap;
        myHashMap = createMyHashMap();

        Node<K, V> currentNodeCopyMap = null;
        Node<K, V> nextCurrentNode;

        for (Bucket<K, V> bucket : copyMyHashMap) {
            if (bucket == null) { continue; }
            do {
                currentNodeCopyMap = currentNodeCopyMap == null ? bucket.node : currentNodeCopyMap;
                nextCurrentNode = currentNodeCopyMap.next == null ? null : currentNodeCopyMap.next;
                insertNodeInNewMap(myHashMap, currentNodeCopyMap);
                currentNodeCopyMap = nextCurrentNode;
            } while (nextCurrentNode != null);
            currentNodeCopyMap = null;
        }
    }

    private void insertNodeInNewMap(Bucket<K, V>[] map, Node<K, V> node) {
        int indexNewBucket = node == null ? 0 : Math.abs(node.hash) % capacity;
        //node.hash = indexNewBucket;
        if (map[indexNewBucket] != null) {
            Node<K, V> lastNode = map[indexNewBucket].node;
            while (lastNode.next != null) {
                lastNode = lastNode.next;
            }
            lastNode.next = node;
            node.next = null;
            size++;
            return;
        }
        if (map[indexNewBucket] == null) {
            map[indexNewBucket] = new Bucket<>(node);
            node.next = null;
            size++;
        }
    }

    private class Bucket<K, V> {
        private Node<K, V> node;

        private Bucket(K key, V value) {
            this.node = new Node<>(key, value);

        }

        private Bucket(Node<K, V> node) {
            this.node = node;
        }
    }

    private class Node<K, V> {
        private int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = key == null ? 0 : Math.abs(key.hashCode());
        }

        private int getHash() {
            return hash;
        }

        private void setHash(int hash) {
            this.hash = hash;
        }
    }
}
