package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int MIN_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75F;
    static final int MULTIPLIER_FOR_RESIZE = 2;
    private Node<K, V>[] bucket;
    private int size = 0;
    private int threshold;

    public MyHashMap() {
        this.bucket = new Node[MIN_INITIAL_CAPACITY];
        this.threshold = (int) (MIN_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 >= threshold) {
            resize();
        }
        int index = getIndex(key);
        if (bucket[index] == null) {
            Node<K, V> node = new Node<>(key, value, null);
            bucket[index] = node;
            size++;
        } else {
            Node<K, V> currentNode = bucket[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(key, value, null);
                    size++;
                    return;

                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = bucket[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        threshold = (int) ((bucket.length * MULTIPLIER_FOR_RESIZE) * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldBucket = bucket;
        bucket = new Node[bucket.length * MULTIPLIER_FOR_RESIZE];
        transfer(oldBucket);
    }

    public void transfer(Node<K, V>[] oldBucket) {
        size = 0;
        for (Node<K, V> bucketNode : oldBucket) {
            while (bucketNode != null) {
                put(bucketNode.key, bucketNode.value);
                bucketNode = bucketNode.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % bucket.length);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
