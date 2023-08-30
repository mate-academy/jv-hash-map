package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] array;
    private int threshold;
    private int size;

    public MyHashMap() {
        array = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value, null);
        if (size == threshold) {
            resize();
        }
        int bucket = findBucket(key);
        Node currentNode = array[bucket];
        if (array[bucket] == null) {
            array[bucket] = node;
        } else {
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, node.key)) {
                    currentNode.value = node.value;
                    return;
                }
                currentNode = currentNode.next;
            }
            node.next = array[bucket];
            array[bucket] = node;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucket = findBucket(key);
        Node<K, V> currentNode = array[bucket];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
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

    private int getKeyHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() * 17);
    }

    private void resize() {
        threshold = (int) (array.length * LOAD_FACTOR);
        Node[] oldArray = array;
        array = new Node[array.length * CAPACITY_MULTIPLIER];
        size = 0;
        for (Node<K, V> node : oldArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int findBucket(K key) {
        return getKeyHash(key) % array.length;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

}
