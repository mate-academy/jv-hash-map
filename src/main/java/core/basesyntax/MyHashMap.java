package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] bucket;
    private int threshold;

    public MyHashMap() {
        bucket = new Node[INITIAL_CAPACITY];
        threshold = (int) (bucket.length * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K keys;
        private V values;
        private Node<K, V> next;

        public Node(K keys, V values, Node<K, V> next) {
            this.keys = keys;
            this.values = values;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getHashCode(key) % bucket.length;
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> currentNode = bucket[index];
        if (currentNode == null) {
            bucket[index] = newNode;
            size++;
        } else {
            while (currentNode.next != null) {
                if (Objects.equals(currentNode.keys, key)) {
                    currentNode.values = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            if (Objects.equals(currentNode.keys, key)) {
                currentNode.values = value;
            } else {
                currentNode.next = newNode;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getHashCode(key) % bucket.length;
        Node<K, V> temporary = bucket[index];
        while (temporary != null) {
            if (Objects.equals(temporary.keys, key)) {
                return temporary.values;
            }
            temporary = temporary.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = bucket.length * 2;
        threshold = threshold * 2;
        Node<K,V>[] newNode = new Node[newCapacity];
        Node<K, V>[] currentBucket = bucket;
        bucket = newNode;
        for (Node<K, V> node : currentBucket) {
            while (node != null) {
                put(node.keys, node.values);
                node = node.next;
                size--;
            }
        }
    }

    public int getHashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % bucket.length);
    }
}
