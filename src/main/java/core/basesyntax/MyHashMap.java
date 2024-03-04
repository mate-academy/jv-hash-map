package core.basesyntax;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int GROW_CONSTANT = 2;

    private List<Node<K, V>>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new List[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int index = getIndex(key);
        if (buckets[index] != null) {
            Node<K, V> lastNode = null;
            for (Node<K, V> node : buckets[index]) {
                lastNode = node;
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
            }
            Node<K, V> newNode = createNewNode(key, value);
            buckets[index].add(newNode);
            if (lastNode != null) {
                lastNode.next = newNode;
            }
        } else {
            buckets[index] = new LinkedList<>();
            buckets[index].add(createNewNode(key, value));
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (buckets[index] != null) {
            for (Node<K, V> node : buckets[index]) {
                if (Objects.equals(node.key, key)) {
                    return node.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    private void resizeIfNeeded() {
        if ((float) size / buckets.length > DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = buckets.length * GROW_CONSTANT;
        List<Node<K, V>>[] newBuckets = new List[newCapacity];
        for (List<Node<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    int newIndex = getHashCode(node.key) % newCapacity;
                    if (newBuckets[newIndex] == null) {
                        newBuckets[newIndex] = new LinkedList<>();
                    }
                    newBuckets[newIndex].add(node);
                }
            }
        }
        buckets = newBuckets;
    }

    private Node<K, V> createNewNode(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        node.hash = getHashCode(key);
        return node;
    }

    private int getIndex(K key) {
        return getHashCode(key) % buckets.length;
    }

    private int getHashCode(K key) {
        return key != null ? Math.abs(key.hashCode()) : 0;
    }

    private static class Node<K, V> {
        private int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
