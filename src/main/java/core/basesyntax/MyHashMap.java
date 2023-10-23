package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_INDEX = 2;

    private int threshold;
    private Node<K, V>[] internalStorage;
    private int size;

    public MyHashMap() {
        this.threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        internalStorage = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private class Node<K, V> {
        private int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return key + " = " + value;
        }
    }

    public final int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> oldBucketNode;
        Node<K, V> inputNode = new Node<>(hash(key), key, value, null);
        int bucketNumber = hash(key) % internalStorage.length;
        if (internalStorage[bucketNumber] == null) {
            internalStorage[bucketNumber] = inputNode;
            size++;
            return;
        }
        oldBucketNode = internalStorage[bucketNumber];
        while (oldBucketNode != null) {
            if (Objects.equals(oldBucketNode.key, key)) {
                oldBucketNode.value = value;
                return;
            }
            if (oldBucketNode.next == null) {
                oldBucketNode.next = inputNode;
                size++;
                resize();
                return;
            }
            oldBucketNode = oldBucketNode.next;
        }
        resize();
    }

    public Node<K, V>[] transfer(Node<K, V>[] oldTab, int oldCap) {
        for (int i = 0; i < oldCap; i++) {
            while (oldTab[i] != null) {
                put(oldTab[i].key, oldTab[i].value);
                oldTab[i] = oldTab[i].next;
            }
        }
        return internalStorage;
    }

    public void resize() {
        if (size == threshold) {
            Node<K, V>[] oldStorage = internalStorage;
            int oldCapacity = oldStorage.length;
            int newCapacity = oldCapacity * RESIZE_INDEX;
            int newThreshold = threshold * RESIZE_INDEX;
            threshold = newThreshold;
            internalStorage = (Node<K, V>[]) new Node[newCapacity];
            size = 0;
            if (oldStorage != null) {
                transfer(oldStorage, oldCapacity);
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> nodeOnIndex;
        nodeOnIndex = internalStorage[hash(key) % internalStorage.length];
        while (nodeOnIndex != null) {
            if (Objects.equals(nodeOnIndex.key, key)) {
                return nodeOnIndex.value;
            }
            nodeOnIndex = nodeOnIndex.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}

